package org.misarch.returns.service

import kotlinx.coroutines.reactor.awaitSingle
import org.misarch.returns.event.EventPublisher
import org.misarch.returns.event.ReturnEvents
import org.misarch.returns.event.model.ReturnDTO
import org.misarch.returns.graphql.AuthorizedUser
import org.misarch.returns.graphql.input.CreateReturnInput
import org.misarch.returns.persistence.model.OrderItemEntity
import org.misarch.returns.persistence.model.ReturnEntity
import org.misarch.returns.persistence.repository.*
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.OffsetDateTime

/**
 * Service for [ReturnEntity]s
 *
 * @param repository the provided repository
 * @param orderItemRepository the order item repository
 * @param shipmentRepository the shipment repository
 * @param orderRepository the order repository
 * @param productVariantVersionRepository the product variant version repository
 * @param eventPublisher the event publisher
 */
@Service
class ReturnService(
    repository: ReturnRepository,
    private val orderItemRepository: OrderItemRepository,
    private val shipmentRepository: ShipmentRepository,
    private val orderRepository: OrderRepository,
    private val productVariantVersionRepository: ProductVariantVersionRepository,
    private val eventPublisher: EventPublisher
) : BaseService<ReturnEntity, ReturnRepository>(repository) {

    /**
     * Creates a return
     * Handles permission checking
     *
     * @param returnInput the return to create
     * @param authorizedUser the authorized user, used for permission checking
     * @return the created return
     */
    suspend fun createReturn(returnInput: CreateReturnInput, authorizedUser: AuthorizedUser): ReturnEntity {
        val orderItems = orderItemRepository.findAllById(returnInput.orderItemIds).collectList().awaitSingle()
        validateReturn(returnInput, orderItems, authorizedUser)
        val orderId = orderItems.first().orderId
        val refundedAmount = orderItems.sumOf { it.compensatableAmount!! }
        val returnEntity = ReturnEntity(
            reason = returnInput.reason, orderId = orderId, refundedAmount = refundedAmount, id = null
        )
        val savedReturn = repository.save(returnEntity).awaitSingle()
        for (orderItem in orderItems) {
            orderItem.returnedWithId = savedReturn.id
        }
        orderItemRepository.saveAll(orderItems).collectList().awaitSingle()
        eventPublisher.publishEvent(
            ReturnEvents.RETURN_CREATED,
            ReturnDTO(savedReturn.id!!, orderId, returnInput.orderItemIds, returnInput.reason, refundedAmount)
        )
        return savedReturn
    }

    /**
     * Checks that a return can be created
     * Checks that at least one order item is provided, and all order items exist, belong to the same order,
     * have been delivered and have not been returned yet
     *
     * @param returnInput the return to validate
     * @param orderItems the order items to validate
     * @param authorizedUser the authorized user, used for permission checking
     */
    private suspend fun validateReturn(
        returnInput: CreateReturnInput, orderItems: MutableList<OrderItemEntity>, authorizedUser: AuthorizedUser
    ) {
        val orderId = orderItems.first().orderId
        val order = orderRepository.findById(orderId).awaitSingle()
        require(order.userId == authorizedUser.id) { "Only the original buyer can create returns" }
        require(returnInput.orderItemIds.isNotEmpty()) { "At least one order item id must be provided" }
        val missingOrderItemIds = returnInput.orderItemIds.toSet() - orderItems.map { it.id }.toSet()
        require(missingOrderItemIds.isEmpty()) { "Order item ids $missingOrderItemIds do not exist" }
        val returnedOrderItems = orderItems.filter { it.returnedWithId != null }
        require(returnedOrderItems.isEmpty()) { "Order item ids ${returnedOrderItems.map { it.id }} have already been returned" }
        require(orderItems.all { it.sentWithId != null && it.compensatableAmount != null && it.productVariantVersionId != null }) {
            "Missing data about order items, please try again later"
        }
        val originalShipments = orderItems.map { it.sentWithId }.toSet()
        val shipmentsById = shipmentRepository.findAllById(originalShipments).collectList().awaitSingle().associateBy { it.id }
        require(shipmentsById.values.all { it.deliveredAt != null }) { "At least one order item was not delivered" }
        require(orderItems.all { it.orderId == orderId }) { "All order items must belong to the same order" }
        for (orderItem in orderItems) {
            val productVariantVersion = productVariantVersionRepository.findById(orderItem.productVariantVersionId!!).awaitSingle()
            require(productVariantVersion.canBeReturnedForDays != null) { "Order item ${orderItem.id} cannot be returned" }
            val passedSeconds = Duration.between(shipmentsById[orderItem.sentWithId]!!.deliveredAt, OffsetDateTime.now()).toSeconds()
            val passedDays = passedSeconds.toDouble() / Duration.ofDays(1).toSeconds()
            require(passedDays <= productVariantVersion.canBeReturnedForDays) { "Order item ${orderItem.id} can no longer be returned" }
        }
    }

}
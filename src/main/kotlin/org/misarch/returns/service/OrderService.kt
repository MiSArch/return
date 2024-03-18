package org.misarch.returns.service

import org.misarch.returns.event.model.OrderDTO
import org.misarch.returns.persistence.model.OrderEntity
import org.misarch.returns.persistence.repository.OrderItemRepository
import org.misarch.returns.persistence.repository.OrderRepository
import org.springframework.stereotype.Service

/**
 * Service for [OrderEntity]s
 *
 * @param repository the provided repository
 * @param orderItemRepository the repository for [OrderItemRepository]s
 */
@Service
class OrderService(
    repository: OrderRepository,
    private val orderItemRepository: OrderItemRepository
) : BaseService<OrderEntity, OrderRepository>(repository) {

    /**
     * Registers a order
     *
     * @param orderDTO the order to register
     */
    suspend fun registerOrder(orderDTO: OrderDTO) {
        repository.createOrder(orderDTO.id, orderDTO.userId)
        orderDTO.orderItems.forEach {
            orderItemRepository.upsertOrderItemFromOrder(
                id = it.id,
                compensatableAmount = it.compensatableAmount,
                orderId = orderDTO.id,
                productVariantVersionId = it.productVariantVersionId
            )
        }
    }

}
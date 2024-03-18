package org.misarch.returns.service

import kotlinx.coroutines.reactor.awaitSingle
import org.misarch.returns.event.model.ShipmentDTO
import org.misarch.returns.event.model.ShipmentStatus
import org.misarch.returns.event.model.ShipmentStatusUpdatedDTO
import org.misarch.returns.persistence.model.ShipmentEntity
import org.misarch.returns.persistence.repository.OrderItemRepository
import org.misarch.returns.persistence.repository.ShipmentRepository
import org.springframework.stereotype.Service
import java.time.OffsetDateTime

/**
 * Service for [ShipmentEntity]s
 *
 * @param repository the provided repository
 * @param orderItemRepository the repository for [OrderItemRepository]s
 */
@Service
class ShipmentService(
    repository: ShipmentRepository,
    private val orderItemRepository: OrderItemRepository
) : BaseService<ShipmentEntity, ShipmentRepository>(repository) {

    /**
     * Registers a shipment, also registers the order items that belong to the shipment
     * Ignores the shipment if the order id is null (belongs to a return)
     *
     * @param shipmentDTO the shipment to register
     */
    suspend fun registerShipment(shipmentDTO: ShipmentDTO) {
        if (shipmentDTO.orderId == null) {
            return
        }
        repository.createShipment(
            shipmentDTO.id,
            if (shipmentDTO.status == ShipmentStatus.DELIVERED) OffsetDateTime.now() else null
        )
        shipmentDTO.orderItemIds.forEach {
            orderItemRepository.upsertOrderItemFromShipment(
                it,
                shipmentDTO.id,
                shipmentDTO.orderId
            )
        }
    }

    /**
     * Updates deliveredAt if the status is DELIVERED
     *
     * @param updateDTO contains the new status and the id of the shipment
     * @return the updated shipment
     */
    suspend fun updateDeliveredAt(updateDTO: ShipmentStatusUpdatedDTO) {
        if (updateDTO.status == ShipmentStatus.DELIVERED) {
            val shipment = repository.findById(updateDTO.id).awaitSingle()
            shipment.deliveredAt = OffsetDateTime.now()
            repository.save(shipment).awaitSingle()
        }
    }

}
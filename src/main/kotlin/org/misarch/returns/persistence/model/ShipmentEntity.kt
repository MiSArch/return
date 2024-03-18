package org.misarch.returns.persistence.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.OffsetDateTime
import java.util.*

/**
 * Entity for a shipment
 *
 * @property deliveredAt the date and time the shipment was delivered, null if not delivered
 * @property id unique identifier of the order item
 */
@Table
class ShipmentEntity(
    var deliveredAt: OffsetDateTime?,
    @Id
    val id: UUID
) {

    companion object {
        /**
         * Querydsl entity
         */
        val ENTITY = QShipmentEntity.shipmentEntity!!
    }

}
package org.misarch.returns.event.model

import java.util.*

/**
 * DTO for a shipment status update
 *
 * @property id id of the shipment
 * @property status new status of the shipment
 */
data class ShipmentStatusUpdatedDTO(
    val id: UUID,
    val status: ShipmentStatus
)
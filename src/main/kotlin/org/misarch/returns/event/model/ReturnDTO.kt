package org.misarch.returns.event.model

import java.util.*

/**
 * Return DTO used for events
 *
 * @property id id of the return
 * @property orderId id of the order of which the items are being returned
 * @property orderItemIds ids of the order items returned
 * @property reason the reason for the return
 * @property refundedAmount the amount of money refunded
 */
data class ReturnDTO(
    val id: UUID,
    val orderId: UUID,
    val orderItemIds: List<UUID>,
    val reason: String,
    val refundedAmount: Long
)
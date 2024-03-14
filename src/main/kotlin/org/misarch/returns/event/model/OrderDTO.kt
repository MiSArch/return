package org.misarch.returns.event.model

import java.util.*

/**
 * DTO of an order of a user.
 *
 * @property id Order UUID.
 * @property userId UUID of user connected with Order.
 * @property orderItems List of OrderItems associated with the Order.
 */
data class OrderDTO(
    val id: UUID,
    val userId: UUID,
    val orderItems: List<OrderItemDTO>
)
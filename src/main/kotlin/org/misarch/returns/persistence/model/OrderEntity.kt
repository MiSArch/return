package org.misarch.returns.persistence.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.*

/**
 * Entity for an order item
 *
 * @property userId id of the user the order is connected to
 * @property id unique identifier of the order item
 */
@Table
class OrderEntity(
    val userId: UUID,
    @Id
    val id: UUID
) {

    companion object {
        /**
         * Querydsl entity
         */
        val ENTITY = QOrderEntity.orderEntity!!
    }

}
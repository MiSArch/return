package org.misarch.returns.persistence.model

import org.misarch.returns.graphql.model.OrderItem
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.*

/**
 * Entity for an order item
 *
 * @property returnedWithId id of the return the order item is part of
 * @property sentWithId id of the shipment this order item was originally sent with
 * @property orderId id of the order the order item is part of
 * @property compensatableAmount the amount of money that can be refunded for the order item
 * @property productVariantVersionId id of the product variant version which is ordered
 * @property id unique identifier of the order item
 */
@Table
class OrderItemEntity(
    var returnedWithId: UUID?,
    val sentWithId: UUID?,
    val orderId: UUID,
    val compensatableAmount: Long?,
    val productVariantVersionId: UUID?,
    @Id
    override val id: UUID
) : BaseEntity<OrderItem> {

    companion object {
        /**
         * Querydsl entity
         */
        val ENTITY = QOrderItemEntity.orderItemEntity!!
    }

    override fun toDTO(): OrderItem {
        return OrderItem(
            id = id,
            returnedWithId = returnedWithId
        )
    }

}
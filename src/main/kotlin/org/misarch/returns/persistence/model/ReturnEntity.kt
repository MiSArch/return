package org.misarch.returns.persistence.model

import org.misarch.returns.graphql.model.Return
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.*

/**
 * Entity for returns
 *
 * @property reason the reason for the return
 * @property orderId the id of the order the return is associated with
 * @property refundedAmount the amount of money refunded
 * @property id unique identifier of the return
 */
@Table
class ReturnEntity(
    val reason: String,
    val orderId: UUID,
    val refundedAmount: Long,
    @Id
    override val id: UUID?
) : BaseEntity<Return> {

    companion object {
        /**
         * Querydsl entity
         */
        val ENTITY = QReturnEntity.returnEntity!!
    }

    override fun toDTO(): Return {
        return Return(
            id = id!!,
            reason = reason,
            orderId = orderId,
            refundedAmount = refundedAmount.toInt()
        )
    }

}
package org.misarch.returns.persistence.model

import org.misarch.returns.event.model.ReturnDTO
import org.misarch.returns.graphql.model.Return
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Entity for returns
 *
 * @property reason the reason for the return
 * @property orderId the id of the order the return is associated with
 * @property refundedAmount the amount of money refunded
 * @property createdAt the time the return was created
 * @property id unique identifier of the return
 */
@Table
class ReturnEntity(
    val reason: String,
    val orderId: UUID,
    val refundedAmount: Long,
    val createdAt: OffsetDateTime,
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
            id = id!!, reason = reason, orderId = orderId, refundedAmount = refundedAmount.toInt()
        )
    }

    /**
     * Converts the entity to an event DTO
     *
     * @param orderItemIds the ids of the order items returned
     * @return event DTO
     */
    fun toEventDTO(orderItemIds: List<UUID>): ReturnDTO {
        return ReturnDTO(
            id = id!!,
            orderId = orderId,
            orderItemIds = orderItemIds,
            reason = reason,
            refundedAmount = refundedAmount,
            createdAt = createdAt.format(
                DateTimeFormatter.ISO_OFFSET_DATE_TIME
            )
        )
    }

}
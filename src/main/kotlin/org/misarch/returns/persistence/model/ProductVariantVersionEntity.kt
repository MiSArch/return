package org.misarch.returns.persistence.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.*

/**
 * Entity for an productVariantVersion item
 *
 * @property canBeReturnedForDays number of days the productVariantVersion can be returned, null if it can be returned forever
 * @property id unique identifier of the productVariantVersion item
 */
@Table
class ProductVariantVersionEntity(
    val canBeReturnedForDays: Int?,
    @Id
    val id: UUID
) {

    companion object {
        /**
         * Querydsl entity
         */
        val ENTITY = QProductVariantVersionEntity.productVariantVersionEntity!!
    }

}
package org.misarch.returns.persistence.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.*

/**
 * Entity for an productVariantVersion item
 *
 * @property userId id of the user the productVariantVersion is connected to
 * @property id unique identifier of the productVariantVersion item
 */
@Table
class ProductVariantVersionEntity(
    val canBeReturnedForDays: Double?,
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
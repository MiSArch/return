package org.misarch.returns.persistence.repository

import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository
import org.misarch.returns.persistence.model.ProductVariantVersionEntity
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Repository for [ProductVariantVersionEntity]s
 */
@Repository
interface ProductVariantVersionRepository : QuerydslR2dbcRepository<ProductVariantVersionEntity, UUID> {

    @Modifying
    @Query("INSERT INTO ProductVariantVersionEntity (id, canBeReturnedForDays) VALUES (:id, :canBeReturnedForDays)")
    suspend fun createProductVariantVersion(
        @Param("id") id: UUID,
        @Param("canBeReturnedForDays") canBeReturnedForDays: Double?
    )

}
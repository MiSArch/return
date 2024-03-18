package org.misarch.returns.persistence.repository

import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository
import org.misarch.returns.persistence.model.ShipmentEntity
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime
import java.util.*

/**
 * Repository for [ShipmentEntity]s
 */
@Repository
interface ShipmentRepository : QuerydslR2dbcRepository<ShipmentEntity, UUID> {

    @Modifying
    @Query("INSERT INTO ShipmentEntity (id, deliveredAt) VALUES (:id, :deliveredAt)")
    suspend fun createShipment(
        @Param("id") id: UUID,
        @Param("deliveredAt") deliveredAt: OffsetDateTime?
    )

}
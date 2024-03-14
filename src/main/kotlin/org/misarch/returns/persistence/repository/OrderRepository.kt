package org.misarch.returns.persistence.repository

import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository
import org.misarch.returns.persistence.model.OrderEntity
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Repository for [OrderEntity]s
 */
@Repository
interface OrderRepository : QuerydslR2dbcRepository<OrderEntity, UUID> {

    @Modifying
    @Query("INSERT INTO OrderEntity (id, userId) VALUES (:id, :userId)")
    suspend fun createOrder(
        @Param("id") id: UUID,
        @Param("userId") userId: UUID
    )

}
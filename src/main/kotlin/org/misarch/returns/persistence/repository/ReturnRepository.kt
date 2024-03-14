package org.misarch.returns.persistence.repository

import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository
import org.misarch.returns.persistence.model.ReturnEntity
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Repository for [ReturnEntity]s
 */
@Repository
interface ReturnRepository : QuerydslR2dbcRepository<ReturnEntity, UUID>
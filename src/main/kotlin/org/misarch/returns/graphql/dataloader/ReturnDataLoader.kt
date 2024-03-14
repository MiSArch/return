package org.misarch.returns.graphql.dataloader

import org.misarch.returns.graphql.model.Return
import org.misarch.returns.persistence.model.ReturnEntity
import org.misarch.returns.persistence.repository.ReturnRepository
import org.springframework.stereotype.Component

/**
 * Data loader for [Return]s
 *
 * @param repository repository for [ReturnEntity]s
 */
@Component
class ReturnDataLoader(
    repository: ReturnRepository
) : IdDataLoader<Return, ReturnEntity>(repository)
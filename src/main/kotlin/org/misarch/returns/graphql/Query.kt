package org.misarch.returns.graphql

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Query
import graphql.schema.DataFetchingEnvironment
import kotlinx.coroutines.reactor.awaitSingle
import org.misarch.returns.graphql.model.Return
import org.misarch.returns.graphql.model.connection.ReturnConnection
import org.misarch.returns.graphql.model.connection.ReturnOrder
import org.misarch.returns.persistence.repository.OrderRepository
import org.misarch.returns.persistence.repository.ReturnRepository
import org.springframework.stereotype.Component
import java.util.*

/**
 * Defines GraphQL queries
 *
 * @property returnRepository The return repository
 */
@Component
class Query(
    private val returnRepository: ReturnRepository,
    private val orderRepository: OrderRepository
) : Query {

    @GraphQLDescription("Get all returns")
    suspend fun returns(
        @GraphQLDescription("Number of items to return")
        first: Int? = null,
        @GraphQLDescription("Number of items to skip")
        skip: Int? = null,
        @GraphQLDescription("Ordering")
        orderBy: ReturnOrder? = null,
        dfe: DataFetchingEnvironment
    ): ReturnConnection {
        return ReturnConnection(first, skip, null, orderBy, returnRepository, dfe.authorizedUserOrNull)
    }

    @GraphQLDescription("Get a return by id")
    suspend fun `return`(
        @GraphQLDescription("The id of the return")
        id: UUID,
        dfe: DataFetchingEnvironment
    ): Return {
        val returnEntity = returnRepository.findById(id).awaitSingle()
        val orderEntity = orderRepository.findById(returnEntity.orderId).awaitSingle()
        val authorizedUser = dfe.authorizedUser
        if (!authorizedUser.isEmployee) {
            require(orderEntity.userId == authorizedUser.id) {
                "You are not authorized to view this return"
            }
        }
        return returnEntity.toDTO()
    }
}
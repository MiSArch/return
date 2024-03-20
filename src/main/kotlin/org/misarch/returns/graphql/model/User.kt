package org.misarch.returns.graphql.model

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.generator.federation.directives.FieldSet
import com.expediagroup.graphql.generator.federation.directives.KeyDirective
import graphql.schema.DataFetchingEnvironment
import org.misarch.returns.graphql.authorizedUserOrNull
import org.misarch.returns.graphql.model.connection.ReturnConnection
import org.misarch.returns.graphql.model.connection.ReturnOrder
import org.misarch.returns.persistence.model.OrderEntity
import org.misarch.returns.persistence.repository.ReturnRepository
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

@GraphQLDescription("A user.")
@KeyDirective(fields = FieldSet("id"))
class User(
    id: UUID
) : Node(id) {

    @GraphQLDescription("Get all returns of this user")
    suspend fun returns(
        @GraphQLDescription("Number of items to return")
        first: Int? = null,
        @GraphQLDescription("Number of items to skip")
        skip: Int? = null,
        @GraphQLDescription("Ordering")
        orderBy: ReturnOrder? = null,
        dfe: DataFetchingEnvironment,
        @GraphQLIgnore
        @Autowired
        returnRepository: ReturnRepository
    ): ReturnConnection {
        return ReturnConnection(
            first, skip, OrderEntity.ENTITY.userId.eq(id), orderBy, returnRepository, dfe.authorizedUserOrNull
        )
    }

}
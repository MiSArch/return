package org.misarch.returns.graphql

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Mutation
import graphql.schema.DataFetchingEnvironment
import org.misarch.returns.graphql.input.CreateReturnInput
import org.misarch.returns.graphql.model.Return
import org.misarch.returns.service.ReturnService
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

/**
 * Defines GraphQL mutations
 *
 * @property returnService used for Return-related mutations
 */
@Component
@Transactional(propagation = Propagation.REQUIRES_NEW)
class Mutation(
    private val returnService: ReturnService
) : Mutation {

    @GraphQLDescription("Create a new return")
    suspend fun createReturn(
        @GraphQLDescription("Input for the createReturn mutation")
        input: CreateReturnInput,
        dfe: DataFetchingEnvironment
    ): Return {
        return returnService.createReturn(input, dfe.authorizedUser).toDTO()
    }

}
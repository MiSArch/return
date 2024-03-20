package org.misarch.returns.graphql.model.connection

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.federation.directives.ShareableDirective
import com.querydsl.core.types.Expression
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.ComparableExpression
import com.querydsl.sql.SQLQuery
import org.misarch.returns.graphql.AuthorizedUser
import org.misarch.returns.graphql.model.Return
import org.misarch.returns.graphql.model.connection.base.BaseConnection
import org.misarch.returns.graphql.model.connection.base.BaseOrder
import org.misarch.returns.graphql.model.connection.base.BaseOrderField
import org.misarch.returns.graphql.model.connection.base.OrderDirection
import org.misarch.returns.persistence.model.OrderEntity
import org.misarch.returns.persistence.model.ReturnEntity
import org.misarch.returns.persistence.repository.ReturnRepository

/**
 * A GraphQL connection for [Return]s.
 *
 * @param first The maximum number of items to return
 * @param skip The number of items to skip
 * @param predicate The predicate to filter the items by
 * @param order The order to sort the items by
 * @param repository The repository to fetch the items from
 * @param authorizedUser The authorized user
 * @param applyJoin A function to apply a join to the query, by default joins with OrderEntity
 */
@GraphQLDescription("A connection to a list of `Return` values.")
@ShareableDirective
class ReturnConnection(
    first: Int?,
    skip: Int?,
    predicate: BooleanExpression?,
    order: ReturnOrder?,
    repository: ReturnRepository,
    authorizedUser: AuthorizedUser?,
    applyJoin: (query: SQLQuery<*>) -> SQLQuery<*> = {
        it.join(OrderEntity.ENTITY).on(ReturnEntity.ENTITY.orderId.eq(OrderEntity.ENTITY.id))
    }
) : BaseConnection<Return, ReturnEntity>(
    first,
    skip,
    null,
    predicate,
    (order ?: ReturnOrder.DEFAULT).toOrderSpecifier(ReturnOrderField.ID),
    repository,
    ReturnEntity.ENTITY,
    authorizedUser,
    applyJoin
) {

    override val primaryKey: ComparableExpression<*> get() = ReturnEntity.ENTITY.id

    override fun authorizedUserFilter(): BooleanExpression? {
        authorizedUser!!
        return if (!authorizedUser.isEmployee) {
            OrderEntity.ENTITY.userId.eq(authorizedUser.id)
        } else {
            null
        }
    }
}

@GraphQLDescription("Return order fields")
enum class ReturnOrderField(override vararg val expressions: Expression<out Comparable<*>>) : BaseOrderField {
    @GraphQLDescription("Order returns by their id")
    ID(ReturnEntity.ENTITY.id),

}

@GraphQLDescription("Return order")
class ReturnOrder(
    direction: OrderDirection?, field: ReturnOrderField?
) : BaseOrder<ReturnOrderField>(direction, field) {

    companion object {
        val DEFAULT = ReturnOrder(OrderDirection.ASC, ReturnOrderField.ID)
    }
}

package org.misarch.returns.graphql.model.connection

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.federation.directives.ShareableDirective
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.ComparableExpression
import com.querydsl.sql.SQLQuery
import org.misarch.returns.graphql.AuthorizedUser
import org.misarch.returns.graphql.model.OrderItem
import org.misarch.returns.graphql.model.connection.base.BaseConnection
import org.misarch.returns.graphql.model.connection.base.CommonOrder
import org.misarch.returns.persistence.model.OrderItemEntity
import org.misarch.returns.persistence.repository.OrderItemRepository

/**
 * A GraphQL connection for [OrderItem]s.
 *
 * @param first The maximum number of items to return
 * @param skip The number of items to skip
 * @param predicate The predicate to filter the items by
 * @param order The order to sort the items by
 * @param repository The repository to fetch the items from
 * @param authorizedUser The authorized user
 * @param applyJoin A function to apply a join to the query
 */
@GraphQLDescription("A connection to a list of `OrderItem` values.")
@ShareableDirective
class OrderItemConnection(
    first: Int?,
    skip: Int?,
    predicate: BooleanExpression?,
    order: CommonOrder?,
    repository: OrderItemRepository,
    authorizedUser: AuthorizedUser?,
    applyJoin: (query: SQLQuery<*>) -> SQLQuery<*> = { it }
) : BaseConnection<OrderItem, OrderItemEntity>(
    first,
    skip,
    null,
    predicate,
    (order ?: CommonOrder.DEFAULT).toOrderSpecifier(OrderItemEntity.ENTITY.id),
    repository,
    OrderItemEntity.ENTITY,
    authorizedUser,
    applyJoin
) {

    override val primaryKey: ComparableExpression<*> get() = OrderItemEntity.ENTITY.id

}
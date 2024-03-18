package org.misarch.returns.graphql.model

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import graphql.schema.DataFetchingEnvironment
import org.misarch.returns.graphql.authorizedUserOrNull
import org.misarch.returns.graphql.model.connection.OrderItemConnection
import org.misarch.returns.graphql.model.connection.base.CommonOrder
import org.misarch.returns.persistence.model.OrderItemEntity
import org.misarch.returns.persistence.repository.OrderItemRepository
import org.springframework.beans.factory.annotation.Autowired
import java.time.OffsetDateTime
import java.util.*

@GraphQLDescription("A return.")
class Return(
    id: UUID,
    @property:GraphQLDescription("The reason for the return.")
    val reason: String,
    @property:GraphQLDescription("The amount of money refunded.")
    val refundedAmount: Int,
    @property:GraphQLDescription("The time the return was created.")
    val createdAt: OffsetDateTime,
    private val orderId: UUID
) : Node(id) {

    @GraphQLDescription("The order this return is for.")
    fun order(): Order {
        return Order(orderId)
    }

    @GraphQLDescription("Get all order items returned with this return")
    fun returnedItems(
        @GraphQLDescription("Number of items to return")
        first: Int? = null,
        @GraphQLDescription("Number of items to skip")
        skip: Int? = null,
        @GraphQLDescription("Ordering")
        orderBy: CommonOrder? = null,
        @GraphQLIgnore
        @Autowired
        orderItemRepository: OrderItemRepository, dfe: DataFetchingEnvironment
    ): OrderItemConnection {
        return OrderItemConnection(
            first,
            skip,
            OrderItemEntity.ENTITY.returnedWithId.eq(id),
            orderBy,
            orderItemRepository,
            dfe.authorizedUserOrNull
        )
    }

}
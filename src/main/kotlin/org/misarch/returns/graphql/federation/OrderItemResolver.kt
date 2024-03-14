package org.misarch.returns.graphql.federation

import com.expediagroup.graphql.generator.federation.execution.FederatedTypeSuspendResolver
import graphql.schema.DataFetchingEnvironment
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.misarch.returns.graphql.model.OrderItem
import org.misarch.returns.persistence.repository.OrderItemRepository
import org.springframework.stereotype.Component
import java.util.*

/**
 * Federated resolver for [OrderItem]s.
 */
@Component
class OrderItemResolver(
    private val orderItemRepository: OrderItemRepository
) : FederatedTypeSuspendResolver<OrderItem> {
    override val typeName: String
        get() = OrderItem::class.simpleName!!

    override suspend fun resolve(
        environment: DataFetchingEnvironment, representation: Map<String, Any>
    ): OrderItem? {
        val id = representation["id"] as String?
        val uuid = id?.let { UUID.fromString(it) }
        return if (id == null) {
            null
        } else {
            orderItemRepository.findById(uuid!!).awaitSingleOrNull()?.toDTO() ?: OrderItem(uuid, null)
        }
    }
}
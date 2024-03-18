package org.misarch.returns.graphql.model

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.federation.directives.FieldSet
import com.expediagroup.graphql.generator.federation.directives.KeyDirective
import java.util.*

@GraphQLDescription("An order.")
@KeyDirective(fields = FieldSet("id"))
class Order(
    id: UUID
) : Node(id)
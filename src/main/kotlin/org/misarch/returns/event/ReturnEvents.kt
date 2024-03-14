package org.misarch.returns.event

/**
 * Constants for return event topics used in the application
 */
object ReturnEvents {
    /**
     * Topic for user creation events (a user has been created)
     */
    const val RETURN_CREATED = "return/return/created"

    /**
     * Topic for shipment creation events (a shipment has been created)
     */
    const val SHIPMENT_CREATED = "shipment/shipment/created"

    /**
     * Topic for shipment status updated events (a shipment status has been updated)
     */
    const val SHIPMENT_STATUS_UPDATED = "shipment/shipment/status-updated"

    /**
     * Topic for order creation events (an order has been created)
     */
    const val ORDER_CREATED = "order/order/created"

    /**
     * Topic for product variant version creation events (a product variant version has been created)
     */
    const val PRODUCT_VARIANT_VERSION_CREATED = "catalog/product-variant-version/created"

    /**
     * Name of the pubsub component
     */
    const val PUBSUB_NAME = "pubsub"
}
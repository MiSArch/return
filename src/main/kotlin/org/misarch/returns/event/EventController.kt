package org.misarch.returns.event

import io.dapr.Topic
import io.dapr.client.domain.CloudEvent
import org.misarch.returns.event.model.OrderDTO
import org.misarch.returns.event.model.ProductVariantVersionDTO
import org.misarch.returns.event.model.ShipmentDTO
import org.misarch.returns.event.model.ShipmentStatusUpdatedDTO
import org.misarch.returns.service.OrderService
import org.misarch.returns.service.ProductVariantVersionService
import org.misarch.returns.service.ShipmentService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Controller for dapr events
 *
 * @param shipmentService the service for [ShipmentDTO]s
 * @param orderService the service for [OrderDTO]s
 * @param productVariantVersionService the service for [ProductVariantVersionDTO]s
 */
@Controller
class EventController(
    private val shipmentService: ShipmentService,
    private val orderService: OrderService,
    private val productVariantVersionService: ProductVariantVersionService
) {

    /**
     * Handles a shipment created event
     *
     * @param cloudEvent the cloud event containing the shipment created
     */
    @Topic(name = ReturnEvents.SHIPMENT_CREATED, pubsubName = ReturnEvents.PUBSUB_NAME)
    @PostMapping("/subscription/${ReturnEvents.SHIPMENT_CREATED}")
    @ResponseStatus(code = HttpStatus.OK)
    suspend fun onShipmentCreated(
        @RequestBody
        cloudEvent: CloudEvent<ShipmentDTO>
    ) {
        shipmentService.registerShipment(cloudEvent.data)
    }

    /**
     * Handles a shipment status updated event
     *
     * @param cloudEvent the cloud event containing the shipment status updated
     */
    @Topic(name = ReturnEvents.SHIPMENT_STATUS_UPDATED, pubsubName = ReturnEvents.PUBSUB_NAME)
    @PostMapping("/subscription/${ReturnEvents.SHIPMENT_STATUS_UPDATED}")
    @ResponseStatus(code = HttpStatus.OK)
    suspend fun onShipmentStatusUpdated(
        @RequestBody
        cloudEvent: CloudEvent<ShipmentStatusUpdatedDTO>
    ) {
        shipmentService.updateDeliveredAt(cloudEvent.data)
    }

    /**
     * Handles an order created event
     *
     * @param cloudEvent the cloud event containing the order created
     */
    @Topic(name = ReturnEvents.ORDER_CREATED, pubsubName = ReturnEvents.PUBSUB_NAME)
    @PostMapping("/subscription/${ReturnEvents.ORDER_CREATED}")
    @ResponseStatus(code = HttpStatus.OK)
    suspend fun onOrderCreated(
        @RequestBody
        cloudEvent: CloudEvent<OrderDTO>
    ) {
        orderService.registerOrder(cloudEvent.data)
    }

    suspend fun onProductVariantVersionCreated(
        @RequestBody
        cloudEvent: CloudEvent<ProductVariantVersionDTO>
    ) {
        productVariantVersionService.registerProductVariantVersion(cloudEvent.data)
    }

}
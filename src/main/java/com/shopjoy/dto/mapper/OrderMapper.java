package com.shopjoy.dto.mapper;

import com.shopjoy.dto.request.CreateOrderRequest;
import com.shopjoy.dto.response.OrderResponse;
import com.shopjoy.entity.Order;


/**
 * Mapper for Order entity and DTOs.
 */
public class OrderMapper {

    /**
     * To order.
     *
     * @param request the request
     * @return the order
     */
    public static Order toOrder(CreateOrderRequest request) {
        if (request == null) {
            return null;
        }
        
        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setShippingAddress(request.getShippingAddress());
        order.setNotes(request.getNotes());
        
        return order;
    }

    /**
     * To order response.
     *
     * @param order the order
     * @return the order response
     */
    public static OrderResponse toOrderResponse(Order order) {
        if (order == null) {
            return null;
        }
        
        return new OrderResponse(
            order.getOrderId(),
            order.getUserId(),
            order.getOrderDate(),
            order.getTotalAmount(),
            order.getStatus(),
            order.getShippingAddress(),
            order.getPaymentMethod(),
            order.getPaymentStatus(),
            order.getNotes(),
            order.getCreatedAt()
        );
    }
    

}

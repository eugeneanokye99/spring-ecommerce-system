package com.shopjoy.dto.mapper;

import com.shopjoy.dto.request.CreateOrderRequest;
import com.shopjoy.dto.request.UpdateOrderRequest;
import com.shopjoy.dto.response.OrderResponse;
import com.shopjoy.entity.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper for Order entity and DTOs.
 */
public class OrderMapper {
    
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
    
    public static List<OrderResponse> toOrderResponseList(List<Order> orders) {
        if (orders == null) {
            return null;
        }
        
        List<OrderResponse> responses = new ArrayList<>();
        for (Order order : orders) {
            responses.add(toOrderResponse(order));
        }
        return responses;
    }
    
    public static void updateOrderFromRequest(Order order, UpdateOrderRequest request) {
        if (order == null || request == null) {
            return;
        }
        
        if (request.getStatus() != null) {
            order.setStatus(request.getStatus());
        }
        
        if (request.getPaymentStatus() != null) {
            order.setPaymentStatus(request.getPaymentStatus());
        }
        
        if (request.getShippingAddress() != null) {
            order.setShippingAddress(request.getShippingAddress());
        }
        
        if (request.getNotes() != null) {
            order.setNotes(request.getNotes());
        }
    }
}

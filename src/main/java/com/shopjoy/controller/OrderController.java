package com.shopjoy.controller;

import com.shopjoy.dto.mapper.OrderMapper;
import com.shopjoy.dto.request.CreateOrderRequest;
import com.shopjoy.dto.response.ApiResponse;
import com.shopjoy.dto.response.OrderResponse;
import com.shopjoy.entity.Order;
import com.shopjoy.entity.OrderItem;
import com.shopjoy.entity.OrderStatus;
import com.shopjoy.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        Order order = OrderMapper.toOrder(request);
        Order createdOrder = orderService.createOrder(
                order.getUserId(),
                null,  // orderItems - needs to be handled separately
                order.getShippingAddress(),
                null   // paymentMethod - not in CreateOrderRequest
        );
        OrderResponse response = OrderMapper.toOrderResponse(createdOrder);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Order created successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(@PathVariable Integer id) {
        Order order = orderService.getOrderById(id);
        OrderResponse response = OrderMapper.toOrderResponse(order);
        return ResponseEntity.ok(ApiResponse.success(response, "Order retrieved successfully"));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersByUser(@PathVariable Integer userId) {
        List<Order> orders = orderService.getOrdersByUser(userId);
        List<OrderResponse> responses = orders.stream()
                .map(OrderMapper::toOrderResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responses, "User orders retrieved successfully"));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersByStatus(@PathVariable OrderStatus status) {
        List<Order> orders = orderService.getOrdersByStatus(status);
        List<OrderResponse> responses = orders.stream()
                .map(OrderMapper::toOrderResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responses, "Orders by status retrieved successfully"));
    }

    @GetMapping("/date-range")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<Order> orders = orderService.getOrdersByDateRange(startDate, endDate);
        List<OrderResponse> responses = orders.stream()
                .map(OrderMapper::toOrderResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responses, "Orders by date range retrieved successfully"));
    }

    @GetMapping("/{orderId}/items")
    public ResponseEntity<ApiResponse<List<OrderItem>>> getOrderItems(@PathVariable Integer orderId) {
        List<OrderItem> items = orderService.getOrderItems(orderId);
        return ResponseEntity.ok(ApiResponse.success(items, "Order items retrieved successfully"));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrderStatus(
            @PathVariable Integer id,
            @RequestParam OrderStatus status) {
        Order updatedOrder = orderService.updateOrderStatus(id, status);
        OrderResponse response = OrderMapper.toOrderResponse(updatedOrder);
        return ResponseEntity.ok(ApiResponse.success(response, "Order status updated successfully"));
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<ApiResponse<OrderResponse>> confirmOrder(@PathVariable Integer id) {
        Order confirmedOrder = orderService.confirmOrder(id);
        OrderResponse response = OrderMapper.toOrderResponse(confirmedOrder);
        return ResponseEntity.ok(ApiResponse.success(response, "Order confirmed successfully"));
    }

    @PatchMapping("/{id}/ship")
    public ResponseEntity<ApiResponse<OrderResponse>> shipOrder(@PathVariable Integer id) {
        Order shippedOrder = orderService.shipOrder(id);
        OrderResponse response = OrderMapper.toOrderResponse(shippedOrder);
        return ResponseEntity.ok(ApiResponse.success(response, "Order shipped successfully"));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<ApiResponse<OrderResponse>> completeOrder(@PathVariable Integer id) {
        Order completedOrder = orderService.completeOrder(id);
        OrderResponse response = OrderMapper.toOrderResponse(completedOrder);
        return ResponseEntity.ok(ApiResponse.success(response, "Order completed successfully"));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(@PathVariable Integer id) {
        Order cancelledOrder = orderService.cancelOrder(id);
        OrderResponse response = OrderMapper.toOrderResponse(cancelledOrder);
        return ResponseEntity.ok(ApiResponse.success(response, "Order cancelled successfully"));
    }

    @GetMapping("/{id}/total")
    public ResponseEntity<ApiResponse<Double>> calculateOrderTotal(@PathVariable Integer id) {
        List<OrderItem> items = orderService.getOrderItems(id);
        double total = orderService.calculateOrderTotal(items);
        return ResponseEntity.ok(ApiResponse.success(total, "Order total calculated successfully"));
    }

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getPendingOrders() {
        List<Order> orders = orderService.getPendingOrders();
        List<OrderResponse> responses = orders.stream()
                .map(OrderMapper::toOrderResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responses, "Pending orders retrieved successfully"));
    }
}

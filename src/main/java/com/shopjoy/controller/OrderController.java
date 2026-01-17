package com.shopjoy.controller;

import com.shopjoy.dto.request.CreateOrderRequest;
import com.shopjoy.dto.response.ApiResponse;
import com.shopjoy.dto.response.OrderResponse;
import com.shopjoy.entity.OrderStatus;
import com.shopjoy.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        OrderResponse response = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Order created successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(@PathVariable Integer id) {
        OrderResponse response = orderService.getOrderById(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Order retrieved successfully"));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersByUser(@PathVariable Integer userId) {
        List<OrderResponse> response = orderService.getOrdersByUser(userId);
        return ResponseEntity.ok(ApiResponse.success(response, "User orders retrieved successfully"));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersByStatus(@PathVariable OrderStatus status) {
        List<OrderResponse> response = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(response, "Orders by status retrieved successfully"));
    }

    @GetMapping("/date-range")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<OrderResponse> response = orderService.getOrdersByDateRange(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(response, "Orders by date range retrieved successfully"));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrderStatus(
            @PathVariable Integer id,
            @RequestParam OrderStatus status) {
        OrderResponse response = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success(response, "Order status updated successfully"));
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<ApiResponse<OrderResponse>> confirmOrder(@PathVariable Integer id) {
        OrderResponse response = orderService.confirmOrder(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Order confirmed successfully"));
    }

    @PatchMapping("/{id}/ship")
    public ResponseEntity<ApiResponse<OrderResponse>> shipOrder(@PathVariable Integer id) {
        OrderResponse response = orderService.shipOrder(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Order shipped successfully"));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<ApiResponse<OrderResponse>> completeOrder(@PathVariable Integer id) {
        OrderResponse response = orderService.completeOrder(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Order completed successfully"));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(@PathVariable Integer id) {
        OrderResponse response = orderService.cancelOrder(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Order cancelled successfully"));
    }

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getPendingOrders() {
        List<OrderResponse> response = orderService.getPendingOrders();
        return ResponseEntity.ok(ApiResponse.success(response, "Pending orders retrieved successfully"));
    }
}

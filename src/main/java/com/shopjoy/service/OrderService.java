package com.shopjoy.service;

import com.shopjoy.entity.Order;
import com.shopjoy.entity.OrderItem;
import com.shopjoy.entity.OrderStatus;
import com.shopjoy.exception.InsufficientStockException;
import com.shopjoy.exception.ResourceNotFoundException;
import com.shopjoy.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service interface for Order-related business operations.
 * Handles order creation, status management, and complex order workflows.
 */
public interface OrderService {
    
    /**
     * Creates a new order for a user.
     * Validates product availability, updates inventory, and calculates total.
     * This is a complex transaction involving multiple entities.
     * 
     * @param userId the user ID
     * @param orderItems list of items to order
     * @param shippingAddress the shipping address
     * @param paymentMethod the payment method
     * @return the created order with generated ID
     * @throws ResourceNotFoundException if user or products not found
     * @throws InsufficientStockException if any product is out of stock
     * @throws ValidationException if order data is invalid
     */
    Order createOrder(Integer userId, List<OrderItem> orderItems, String shippingAddress, String paymentMethod);
    
    /**
     * Retrieves an order by its ID.
     * 
     * @param orderId the order ID
     * @return the order
     * @throws ResourceNotFoundException if order not found
     */
    Order getOrderById(Integer orderId);
    
    /**
     * Retrieves all orders for a specific user.
     * 
     * @param userId the user ID
     * @return list of user's orders
     */
    List<Order> getOrdersByUser(Integer userId);
    
    /**
     * Retrieves all orders with a specific status.
     * 
     * @param status the order status
     * @return list of orders with the status
     */
    List<Order> getOrdersByStatus(OrderStatus status);
    
    /**
     * Retrieves orders within a date range.
     * 
     * @param startDate start date (inclusive)
     * @param endDate end date (inclusive)
     * @return list of orders in the date range
     */
    List<Order> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Retrieves all order items for a specific order.
     * 
     * @param orderId the order ID
     * @return list of order items
     * @throws ResourceNotFoundException if order not found
     */
    List<OrderItem> getOrderItems(Integer orderId);
    
    /**
     * Updates an order's status.
     * Validates that the status transition is allowed.
     * 
     * @param orderId the order ID
     * @param newStatus the new status
     * @return the updated order
     * @throws ResourceNotFoundException if order not found
     * @throws InvalidOrderStateException if status transition is invalid
     */
    Order updateOrderStatus(Integer orderId, OrderStatus newStatus);
    
    /**
     * Confirms a pending order (moves to CONFIRMED status).
     * 
     * @param orderId the order ID
     * @return the updated order
     * @throws ResourceNotFoundException if order not found
     * @throws InvalidOrderStateException if order is not pending
     */
    Order confirmOrder(Integer orderId);
    
    /**
     * Ships an order (moves to SHIPPED status).
     * 
     * @param orderId the order ID
     * @return the updated order
     * @throws ResourceNotFoundException if order not found
     * @throws InvalidOrderStateException if order is not confirmed
     */
    Order shipOrder(Integer orderId);
    
    /**
     * Completes an order (moves to COMPLETED status).
     * 
     * @param orderId the order ID
     * @return the updated order
     * @throws ResourceNotFoundException if order not found
     * @throws InvalidOrderStateException if order is not shipped
     */
    Order completeOrder(Integer orderId);
    
    /**
     * Cancels an order and restores inventory.
     * Can only cancel pending or confirmed orders.
     * 
     * @param orderId the order ID
     * @return the updated order
     * @throws ResourceNotFoundException if order not found
     * @throws InvalidOrderStateException if order cannot be cancelled
     */
    Order cancelOrder(Integer orderId);
    
    /**
     * Calculates the total amount for an order.
     * Can include business logic for discounts, taxes, shipping costs.
     * 
     * @param orderItems list of order items
     * @return the calculated total
     */
    double calculateOrderTotal(List<OrderItem> orderItems);
    
    /**
     * Retrieves pending orders (requires action).
     * 
     * @return list of pending orders
     */
    List<Order> getPendingOrders();
}

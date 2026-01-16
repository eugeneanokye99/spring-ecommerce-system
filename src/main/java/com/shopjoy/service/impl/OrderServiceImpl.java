package com.shopjoy.service.impl;

import com.shopjoy.entity.Order;
import com.shopjoy.entity.OrderItem;
import com.shopjoy.entity.OrderStatus;
import com.shopjoy.entity.PaymentStatus;
import com.shopjoy.exception.InvalidOrderStateException;
import com.shopjoy.exception.ResourceNotFoundException;
import com.shopjoy.exception.ValidationException;
import com.shopjoy.repository.OrderItemRepository;
import com.shopjoy.repository.OrderRepository;
import com.shopjoy.service.InventoryService;
import com.shopjoy.service.OrderService;
import com.shopjoy.service.ProductService;
import com.shopjoy.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final InventoryService inventoryService;
    private final ProductService productService;
    private final UserService userService;
    
    public OrderServiceImpl(OrderRepository orderRepository,
                           OrderItemRepository orderItemRepository,
                           InventoryService inventoryService,
                           ProductService productService,
                           UserService userService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.inventoryService = inventoryService;
        this.productService = productService;
        this.userService = userService;
    }
    
    /**
     * COMPLEX TRANSACTION EXAMPLE
     * 
     * This method demonstrates a multi-entity transaction:
     * 1. Validates user exists
     * 2. Validates all products exist and are active
     * 3. Checks inventory availability for all items
     * 4. Reserves inventory (decreases stock)
     * 5. Creates order
     * 6. Creates order items
     * 
     * If ANY step fails, entire transaction is rolled back.
     * Uses SERIALIZABLE isolation to prevent phantom reads during stock checks.
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE)
    public Order createOrder(Integer userId, List<OrderItem> orderItems, 
                            String shippingAddress, String paymentMethod) {
        logger.info("Creating order for user ID: {} with {} items", userId, orderItems.size());
        
        userService.getUserById(userId);
        
        if (orderItems == null || orderItems.isEmpty()) {
            throw new ValidationException("Order must contain at least one item");
        }
        
        if (shippingAddress == null || shippingAddress.trim().isEmpty()) {
            throw new ValidationException("Shipping address is required");
        }
        
        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            throw new ValidationException("Payment method is required");
        }
        
        for (OrderItem item : orderItems) {
            if (item.getQuantity() <= 0) {
                throw new ValidationException("Item quantity must be positive");
            }
            
            productService.getProductById(item.getProductId());
            
            if (!inventoryService.hasAvailableStock(item.getProductId(), item.getQuantity())) {
                throw new ValidationException(
                    String.format("Insufficient stock for product ID: %d", item.getProductId()));
            }
        }
        
        double totalAmount = calculateOrderTotal(orderItems);
        
        Order order = Order.builder()
                .userId(userId)
                .orderDate(LocalDateTime.now())
                .totalAmount(totalAmount)
                .status(OrderStatus.PENDING)
                .shippingAddress(shippingAddress)
                .paymentMethod(paymentMethod)
                .paymentStatus(PaymentStatus.UNPAID)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        Order createdOrder = orderRepository.save(order);
        logger.info("Created order with ID: {}", createdOrder.getOrderId());
        
        for (OrderItem item : orderItems) {
            item.setOrderId(createdOrder.getOrderId());
            item.setCreatedAt(LocalDateTime.now());
            orderItemRepository.save(item);
            
            inventoryService.reserveStock(item.getProductId(), item.getQuantity());
            logger.debug("Reserved {} units of product ID: {}", item.getQuantity(), item.getProductId());
        }
        
        logger.info("Successfully created order ID: {} with total: {}", 
                createdOrder.getOrderId(), totalAmount);
        
        return createdOrder;
    }
    
    @Override
    public Order getOrderById(Integer orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
    }
    
    @Override
    public List<Order> getOrdersByUser(Integer userId) {
        return orderRepository.findByUserId(userId);
    }
    
    @Override
    public List<Order> getOrdersByStatus(OrderStatus status) {
        if (status == null) {
            throw new ValidationException("Order status cannot be null");
        }
        return orderRepository.findByStatus(status);
    }
    
    @Override
    public List<Order> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            throw new ValidationException("Start and end dates cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new ValidationException("Start date must be before end date");
        }
        return orderRepository.findByDateRange(startDate, endDate);
    }
    
    @Override
    public List<OrderItem> getOrderItems(Integer orderId) {
        getOrderById(orderId);
        return orderItemRepository.findByOrderId(orderId);
    }
    
    /**
     * STATE MACHINE PATTERN EXAMPLE
     * 
     * Order status transitions follow a specific workflow:
     * PENDING -> PROCESSING -> SHIPPED -> DELIVERED
     *         -> CANCELLED (from PENDING or PROCESSING only)
     */
    @Override
    @Transactional(readOnly = false)
    public Order updateOrderStatus(Integer orderId, OrderStatus newStatus) {
        logger.info("Updating order {} status to: {}", orderId, newStatus);
        
        Order order = getOrderById(orderId);
        OrderStatus currentStatus = order.getStatus();
        
        validateStatusTransition(currentStatus, newStatus);
        
        order.setStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());
        
        Order updatedOrder = orderRepository.update(order);
        logger.info("Successfully updated order {} status to: {}", orderId, newStatus);
        
        return updatedOrder;
    }
    
    @Override
    @Transactional(readOnly = false)
    public Order confirmOrder(Integer orderId) {
        Order order = getOrderById(orderId);
        
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new InvalidOrderStateException(orderId, order.getStatus().toString(), "confirm");
        }
        
        return updateOrderStatus(orderId, OrderStatus.PROCESSING);
    }
    
    @Override
    @Transactional(readOnly = false)
    public Order shipOrder(Integer orderId) {
        Order order = getOrderById(orderId);
        
        if (order.getStatus() != OrderStatus.PROCESSING) {
            throw new InvalidOrderStateException(orderId, order.getStatus().toString(), "ship");
        }
        
        return updateOrderStatus(orderId, OrderStatus.SHIPPED);
    }
    
    @Override
    @Transactional(readOnly = false)
    public Order completeOrder(Integer orderId) {
        Order order = getOrderById(orderId);
        
        if (order.getStatus() != OrderStatus.SHIPPED) {
            throw new InvalidOrderStateException(orderId, order.getStatus().toString(), "complete");
        }
        
        return updateOrderStatus(orderId, OrderStatus.DELIVERED);
    }
    
    /**
     * COMPLEX ROLLBACK SCENARIO
     * 
     * When cancelling an order, we must:
     * 1. Validate order can be cancelled
     * 2. Release reserved inventory back to stock
     * 3. Update order status
     * 
     * If inventory release fails, entire transaction rolls back.
     */
    @Override
    @Transactional(readOnly = false)
    public Order cancelOrder(Integer orderId) {
        logger.info("Cancelling order ID: {}", orderId);
        
        Order order = getOrderById(orderId);
        
        if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.PROCESSING) {
            throw new InvalidOrderStateException(
                orderId, 
                order.getStatus().toString(), 
                "cancel (can only cancel PENDING or PROCESSING orders)");
        }
        
        List<OrderItem> orderItems = getOrderItems(orderId);
        
        for (OrderItem item : orderItems) {
            inventoryService.releaseStock(item.getProductId(), item.getQuantity());
            logger.debug("Released {} units of product ID: {}", item.getQuantity(), item.getProductId());
        }
        
        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());
        
        Order cancelledOrder = orderRepository.update(order);
        logger.info("Successfully cancelled order ID: {}", orderId);
        
        return cancelledOrder;
    }
    
    @Override
    public double calculateOrderTotal(List<OrderItem> orderItems) {
        if (orderItems == null || orderItems.isEmpty()) {
            return 0.0;
        }
        
        return orderItems.stream()
                .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
                .sum();
    }
    
    @Override
    public List<Order> getPendingOrders() {
        return getOrdersByStatus(OrderStatus.PENDING);
    }
    
    private void validateStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        if (currentStatus == OrderStatus.CANCELLED || currentStatus == OrderStatus.DELIVERED) {
            throw new InvalidOrderStateException(
                0, 
                currentStatus.toString(), 
                "Cannot change status of cancelled or delivered orders");
        }
        
        switch (newStatus) {
            case PENDING:
                // PENDING is the initial state, no validation needed
                break;
            case PROCESSING:
                if (currentStatus != OrderStatus.PENDING) {
                    throw new InvalidOrderStateException(
                        0, currentStatus.toString(), 
                        "Can only process PENDING orders");
                }
                break;
            case SHIPPED:
                if (currentStatus != OrderStatus.PROCESSING) {
                    throw new InvalidOrderStateException(
                        0, currentStatus.toString(), 
                        "Can only ship PROCESSING orders");
                }
                break;
            case DELIVERED:
                if (currentStatus != OrderStatus.SHIPPED) {
                    throw new InvalidOrderStateException(
                        0, currentStatus.toString(), 
                        "Can only deliver SHIPPED orders");
                }
                break;
            case CANCELLED:
                if (currentStatus != OrderStatus.PENDING && currentStatus != OrderStatus.PROCESSING) {
                    throw new InvalidOrderStateException(
                        0, currentStatus.toString(), 
                        "Can only cancel PENDING or PROCESSING orders");
                }
                break;
        }
    }
}

package com.shopjoy.dto.response;

import com.shopjoy.entity.OrderStatus;
import com.shopjoy.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Response DTO for Order.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    
    private int orderId;
    private int userId;
    private LocalDateTime orderDate;
    private double totalAmount;
    private OrderStatus status;
    private String shippingAddress;
    private String paymentMethod;
    private PaymentStatus paymentStatus;
    private String notes;
    private LocalDateTime createdAt;


}

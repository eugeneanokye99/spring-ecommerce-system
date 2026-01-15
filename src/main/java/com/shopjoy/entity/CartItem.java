package com.shopjoy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    private int cartItemId;
    private int userId;
    private int productId;
    private int quantity;
    private LocalDateTime createdAt;

    // Transient fields for display
    private Product product;
}

package com.shopjoy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The type Cart item.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItem implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int cartItemId;
    private int userId;
    private int productId;
    private int quantity;
    private LocalDateTime createdAt;
    private Product product;
}

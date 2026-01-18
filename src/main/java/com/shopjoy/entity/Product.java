package com.shopjoy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The type Product.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int productId;
    private String productName;
    private String description;
    private int categoryId;
    private double price;
    private double costPrice;
    private String sku;
    private String brand;
    private String imageUrl;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

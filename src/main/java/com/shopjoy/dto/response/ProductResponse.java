package com.shopjoy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Response DTO for Product.
 * This is what the API sends back to clients when they request product information.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private int productId;
    private String productName;
    private String description;
    private int categoryId;
    private double price;
    private double costPrice;
    private String sku;
    private String brand;
    private String imageUrl;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}

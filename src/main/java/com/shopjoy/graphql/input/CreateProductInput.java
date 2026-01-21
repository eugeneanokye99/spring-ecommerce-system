package com.shopjoy.graphql.input;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateProductInput(
        @NotBlank(message = "Product name is required")
        String name,
        
        String description,
        
        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
        BigDecimal price,
        
        @NotNull(message = "Stock quantity is required")
        @Min(value = 0, message = "Stock quantity must be non-negative")
        Integer stockQuantity,
        
        Long categoryId
) {}

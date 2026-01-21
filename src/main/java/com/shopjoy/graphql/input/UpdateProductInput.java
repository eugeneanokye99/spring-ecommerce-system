package com.shopjoy.graphql.input;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

public record UpdateProductInput(
        String name,
        String description,
        
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
        BigDecimal price,
        
        @Min(value = 0, message = "Stock quantity must be non-negative")
        Integer stockQuantity,
        
        Long categoryId
) {}

package com.shopjoy.graphql.input;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateReviewInput(
        @NotNull(message = "Product ID is required")
        Long productId,
        
        @NotNull(message = "User ID is required")
        Long userId,
        
        @NotNull(message = "Rating is required")
        @Min(value = 1, message = "Rating must be between 1 and 5")
        @Max(value = 5, message = "Rating must be between 1 and 5")
        Integer rating,
        
        String comment
) {}

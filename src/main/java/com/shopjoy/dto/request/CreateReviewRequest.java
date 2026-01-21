package com.shopjoy.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Request DTO for creating a product review.
 */
@Setter
@Getter
@Builder
public class CreateReviewRequest {
    
    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be positive")
    private Integer userId;
    
    @NotNull(message = "Product ID is required")
    @Positive(message = "Product ID must be positive")
    private Integer productId;
    
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot exceed 5")
    private Integer rating;
    
    @NotBlank(message = "Review title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;
    
    @Size(max = 1000, message = "Comment cannot exceed 1000 characters")
    private String comment;

 

}

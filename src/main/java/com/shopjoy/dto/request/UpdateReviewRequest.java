package com.shopjoy.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Request DTO for updating a review.
 */
@Setter
@Getter
public class UpdateReviewRequest {
    
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot exceed 5")
    private Integer rating;
    
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;
    
    @Size(max = 1000, message = "Comment cannot exceed 1000 characters")
    private String comment;

    /**
     * Instantiates a new Update review request.
     */
    public UpdateReviewRequest() {
    }

}

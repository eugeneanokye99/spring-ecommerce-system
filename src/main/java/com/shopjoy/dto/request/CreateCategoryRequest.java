package com.shopjoy.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Request DTO for creating a new category.
 */
@Setter
@Getter
public class CreateCategoryRequest {
    
    @NotBlank(message = "Category name is required")
    @Size(min = 2, max = 100, message = "Category name must be between 2 and 100 characters")
    private String categoryName;
    
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
    
    @Positive(message = "Parent category ID must be positive")
    private Integer parentCategoryId;

    /**
     * Instantiates a new Create category request.
     */
    public CreateCategoryRequest() {
    }

}

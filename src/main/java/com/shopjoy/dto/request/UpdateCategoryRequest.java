package com.shopjoy.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Request DTO for updating an existing category.
 */
@Setter
@Getter
public class UpdateCategoryRequest {
    
    @Size(min = 2, max = 100, message = "Category name must be between 2 and 100 characters")
    private String categoryName;
    
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    /**
     * Instantiates a new Update category request.
     */
    public UpdateCategoryRequest() {
    }

}

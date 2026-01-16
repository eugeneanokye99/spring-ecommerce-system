package com.shopjoy.dto.request;

import jakarta.validation.constraints.Size;

/**
 * Request DTO for updating an existing category.
 */
public class UpdateCategoryRequest {
    
    @Size(min = 2, max = 100, message = "Category name must be between 2 and 100 characters")
    private String categoryName;
    
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
    
    public UpdateCategoryRequest() {
    }
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
}

package com.shopjoy.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating a new category.
 */
public class CreateCategoryRequest {
    
    @NotBlank(message = "Category name is required")
    @Size(min = 2, max = 100, message = "Category name must be between 2 and 100 characters")
    private String categoryName;
    
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
    
    @Positive(message = "Parent category ID must be positive")
    private Integer parentCategoryId;
    
    public CreateCategoryRequest() {
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
    
    public Integer getParentCategoryId() {
        return parentCategoryId;
    }
    
    public void setParentCategoryId(Integer parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }
}

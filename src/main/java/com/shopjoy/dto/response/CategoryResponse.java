package com.shopjoy.dto.response;

import java.time.LocalDateTime;

/**
 * Response DTO for Category.
 */
public class CategoryResponse {
    
    private int categoryId;
    private String categoryName;
    private String description;
    private Integer parentCategoryId;
    private LocalDateTime createdAt;
    
    public CategoryResponse() {
    }
    
    public CategoryResponse(int categoryId, String categoryName, String description, 
                           Integer parentCategoryId, LocalDateTime createdAt) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.description = description;
        this.parentCategoryId = parentCategoryId;
        this.createdAt = createdAt;
    }
    
    public int getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
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
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

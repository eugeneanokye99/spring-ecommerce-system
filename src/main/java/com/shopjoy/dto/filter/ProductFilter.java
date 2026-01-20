package com.shopjoy.dto.filter;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Filter criteria for product queries")
public class ProductFilter {
    
    @Schema(description = "Minimum price filter", example = "100.00")
    private Double minPrice;
    
    @Schema(description = "Maximum price filter", example = "1000.00")
    private Double maxPrice;
    
    @Schema(description = "Category ID filter", example = "1")
    private Integer categoryId;
    
    @Schema(description = "Search term for product name or description", example = "Laptop")
    private String searchTerm;
    
    @Schema(description = "Filter by stock availability", example = "true")
    private Boolean inStock;
    
    @Schema(description = "Minimum stock quantity", example = "10")
    private Integer minStock;
    
    @Schema(description = "Maximum stock quantity", example = "100")
    private Integer maxStock;
    
    @Schema(description = "Filter by active status", example = "true")
    private Boolean isActive;
    
    public ProductFilter() {
    }
    
    public Double getMinPrice() {
        return minPrice;
    }
    
    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }
    
    public Double getMaxPrice() {
        return maxPrice;
    }
    
    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }
    
    public Integer getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
    
    public String getSearchTerm() {
        return searchTerm;
    }
    
    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }
    
    public Boolean getInStock() {
        return inStock;
    }
    
    public void setInStock(Boolean inStock) {
        this.inStock = inStock;
    }
    
    public Integer getMinStock() {
        return minStock;
    }
    
    public void setMinStock(Integer minStock) {
        this.minStock = minStock;
    }
    
    public Integer getMaxStock() {
        return maxStock;
    }
    
    public void setMaxStock(Integer maxStock) {
        this.maxStock = maxStock;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}

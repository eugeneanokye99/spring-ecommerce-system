package com.shopjoy.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating a new product.
 * Clients send this when adding a new product to the catalog.
 */
public class CreateProductRequest {
    
    // Product name: required, 3-200 characters
    @NotBlank(message = "Product name is required")
    @Size(min = 3, max = 200, message = "Product name must be between 3 and 200 characters")
    private String productName;
    
    // Description: optional, max 2000 characters
    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;
    
    // Category ID: required, must be positive
    @NotNull(message = "Category ID is required")
    @Positive(message = "Category ID must be positive")
    private Integer categoryId;
    
    // Price: required, must be positive
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;
    
    // Cost price: optional, but must be positive if provided
    @Positive(message = "Cost price must be positive")
    private Double costPrice;
    
    // SKU: optional
    @Size(max = 100, message = "SKU cannot exceed 100 characters")
    private String sku;
    
    // Brand: optional
    @Size(max = 100, message = "Brand cannot exceed 100 characters")
    private String brand;
    
    // Image URL: optional
    @Size(max = 500, message = "Image URL cannot exceed 500 characters")
    private String imageUrl;
    
    // Is active: optional, defaults to true
    private Boolean isActive = true;
    
    // No-argument constructor
    public CreateProductRequest() {
    }
    
    // Getters and Setters
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
    
    public Double getPrice() {
        return price;
    }
    
    public void setPrice(Double price) {
        this.price = price;
    }
    
    public Double getCostPrice() {
        return costPrice;
    }
    
    public void setCostPrice(Double costPrice) {
        this.costPrice = costPrice;
    }
    
    public String getSku() {
        return sku;
    }
    
    public void setSku(String sku) {
        this.sku = sku;
    }
    
    public String getBrand() {
        return brand;
    }
    
    public void setBrand(String brand) {
        this.brand = brand;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}

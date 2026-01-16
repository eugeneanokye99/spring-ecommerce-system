package com.shopjoy.dto.request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for updating an existing product.
 * All fields are optional - only send fields you want to update.
 */
public class UpdateProductRequest {
    
    // Product name: optional
    @Size(min = 3, max = 200, message = "Product name must be between 3 and 200 characters")
    private String productName;
    
    // Description: optional
    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;
    
    // Price: optional, but must be positive if provided
    @Positive(message = "Price must be positive")
    private Double price;
    
    // Cost price: optional, but must be positive if provided
    @Positive(message = "Cost price must be positive")
    private Double costPrice;
    
    // Brand: optional
    @Size(max = 100, message = "Brand cannot exceed 100 characters")
    private String brand;
    
    // Image URL: optional
    @Size(max = 500, message = "Image URL cannot exceed 500 characters")
    private String imageUrl;
    
    // Is active: optional
    private Boolean isActive;
    
    // No-argument constructor
    public UpdateProductRequest() {
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

package com.shopjoy.dto.request;

import com.shopjoy.validation.ValidPrice;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Request DTO for creating a new product.
 * Clients send this when adding a new product to the catalog.
 * 
 * Uses custom validators:
 * - @ValidPrice ensures price is positive with max 2 decimal places
 */
@Setter
@Getter
public class CreateProductRequest {

    @NotBlank(message = "Product name is required")
    @Size(min = 3, max = 200, message = "Product name must be between 3 and 200 characters")
    private String productName;
    
    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;
    
    @NotNull(message = "Category ID is required")
    @Positive(message = "Category ID must be positive")
    private Integer categoryId;
    
    @NotNull(message = "Price is required")
    @ValidPrice(message = "Price must be positive with at most 2 decimal places")
    private Double price;
    
    @ValidPrice(allowNull = true, message = "Cost price must be positive with at most 2 decimal places")
    private Double costPrice;
    
    @NotBlank(message = "SKU is required")
    @Size(min = 3, max = 100, message = "SKU must be between 3 and 100 characters")
    private String sku;
    
    @Size(max = 100, message = "Brand cannot exceed 100 characters")
    private String brand;
    
    @Size(max = 500, message = "Image URL cannot exceed 500 characters")
    private String imageUrl;
    
    private Boolean isActive = true;

    public CreateProductRequest() {
    }

}

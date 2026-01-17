package com.shopjoy.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Request DTO for creating a new product.
 * Clients send this when adding a new product to the catalog.
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
    @Positive(message = "Price must be positive")
    private Double price;
    
    @Positive(message = "Cost price must be positive")
    private Double costPrice;
    
    @Size(max = 100, message = "SKU cannot exceed 100 characters")
    private String sku;
    
    @Size(max = 100, message = "Brand cannot exceed 100 characters")
    private String brand;
    
    @Size(max = 500, message = "Image URL cannot exceed 500 characters")
    private String imageUrl;
    
    private Boolean isActive = true;

    /**
     * Instantiates a new Create product request.
     */
    public CreateProductRequest() {
    }

}

package com.shopjoy.dto.request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Request DTO for updating an existing product.
 * All fields are optional - only send fields you want to update.
 */
@Setter
@Getter
public class UpdateProductRequest {


    @Size(min = 3, max = 200, message = "Product name must be between 3 and 200 characters")
    private String productName;
    
    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;
    
    @Positive(message = "Price must be positive")
    private Double price;
    
    @Positive(message = "Cost price must be positive")
    private Double costPrice;
    
    @Size(max = 100, message = "Brand cannot exceed 100 characters")
    private String brand;
    
    @Size(max = 500, message = "Image URL cannot exceed 500 characters")
    private String imageUrl;
    
    private Boolean isActive;

    /**
     * Instantiates a new Update product request.
     */
    public UpdateProductRequest() {
    }

}

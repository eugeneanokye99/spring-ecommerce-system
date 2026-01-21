package com.shopjoy.dto.request;

import com.shopjoy.validation.ValidPrice;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "Request for creating a new product with all required details")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductRequest {

    @Schema(description = "Product name", example = "Dell XPS 15 Laptop", required = true)
    @NotBlank(message = "Product name is required")
    @Size(min = 3, max = 200, message = "Product name must be between 3 and 200 characters")
    private String productName;
    
    @Schema(description = "Detailed product description", example = "High-performance laptop with Intel Core i7 processor, 16GB RAM, and 512GB SSD")
    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;
    
    @Schema(description = "Category unique identifier", example = "1", required = true)
    @NotNull(message = "Category ID is required")
    @Positive(message = "Category ID must be positive")
    private Integer categoryId;
    
    @Schema(description = "Product selling price", example = "1299.99", required = true)
    @NotNull(message = "Price is required")
    @ValidPrice(message = "Price must be positive with at most 2 decimal places")
    private Double price;
    
    @Schema(description = "Product cost price (wholesale/manufacturing cost)", example = "899.99")
    @ValidPrice(allowNull = true, message = "Cost price must be positive with at most 2 decimal places")
    private Double costPrice;
    
    @Schema(description = "Stock Keeping Unit (unique product identifier)", example = "LAPTOP-DELL-XPS15-001", required = true)
    @NotBlank(message = "SKU is required")
    @Size(min = 3, max = 100, message = "SKU must be between 3 and 100 characters")
    private String sku;
    
    @Schema(description = "Product brand name", example = "Dell")
    @Size(max = 100, message = "Brand cannot exceed 100 characters")
    private String brand;
    
    @Schema(description = "Product image URL", example = "https://example.com/images/dell-xps15.jpg")
    @Size(max = 500, message = "Image URL cannot exceed 500 characters")
    private String imageUrl;
    
    @Schema(description = "Whether the product is active and available for sale", example = "true", defaultValue = "true")
    private Boolean isActive = true;



}

package com.shopjoy.dto.filter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    


}

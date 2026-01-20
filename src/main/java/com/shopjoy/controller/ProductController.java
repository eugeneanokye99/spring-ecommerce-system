package com.shopjoy.controller;

import com.shopjoy.dto.filter.ProductFilter;
import com.shopjoy.dto.request.CreateProductRequest;
import com.shopjoy.dto.request.UpdateProductRequest;
import com.shopjoy.dto.response.ApiResponse;
import com.shopjoy.dto.response.ProductResponse;
import com.shopjoy.service.ProductService;
import com.shopjoy.util.Page;
import com.shopjoy.util.Pageable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Product Management", description = "APIs for managing products including CRUD operations, pagination, filtering, sorting, and search capabilities with advanced algorithms")
@Validated
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(
            summary = "Create a new product",
            description = "Creates a new product with the provided details including name, description, price, category, and stock information"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Product created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductResponse.class),
                            examples = @ExampleObject(
                                    name = "Product Created",
                                    value = "{\"success\":true,\"message\":\"Product created successfully\",\"data\":{\"productId\":1,\"name\":\"Laptop\",\"price\":999.99},\"timestamp\":\"2024-01-20T10:30:00\"}"
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data or validation error",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Validation Error",
                                    value = "{\"success\":false,\"message\":\"Validation failed\",\"errors\":[{\"field\":\"price\",\"message\":\"Price must be positive\"}],\"timestamp\":\"2024-01-20T10:30:00\"}"
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "Product with the same name already exists",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody CreateProductRequest request) {
        ProductResponse response = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Product created successfully"));
    }

    @Operation(
            summary = "Get product by ID",
            description = "Retrieves a product by its unique identifier"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Product retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Product not found",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid product ID format",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(
            @Parameter(description = "Product unique identifier", required = true, example = "1")
            @PathVariable @Positive(message = "Product ID must be positive") Integer id) {
        ProductResponse response = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Product retrieved successfully"));
    }

    @Operation(
            summary = "Get all products",
            description = "Retrieves a complete list of all products in the system"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Products retrieved successfully",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts() {
        List<ProductResponse> response = productService.getAllProducts();
        return ResponseEntity.ok(ApiResponse.success(response, "Products retrieved successfully"));
    }

    @Operation(
            summary = "Get all active products",
            description = "Retrieves only products that are currently active and available for sale"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Active products retrieved successfully",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getActiveProducts() {
        List<ProductResponse> response = productService.getActiveProducts();
        return ResponseEntity.ok(ApiResponse.success(response, "Active products retrieved successfully"));
    }

    @Operation(
            summary = "Get products by category",
            description = "Retrieves all products belonging to a specific category"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Products by category retrieved successfully",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Category not found",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProductsByCategory(
            @Parameter(description = "Category unique identifier", required = true, example = "1")
            @PathVariable Integer categoryId) {
        List<ProductResponse> response = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success(response, "Products by category retrieved successfully"));
    }

    @Operation(
            summary = "Search products by name",
            description = "Searches for products whose name contains the specified keyword (case-insensitive)"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Products search completed successfully",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> searchProductsByName(
            @Parameter(description = "Product name keyword to search for", required = true, example = "Laptop")
            @RequestParam String name) {
        List<ProductResponse> response = productService.searchProductsByName(name);
        return ResponseEntity.ok(ApiResponse.success(response, "Products search completed successfully"));
    }

    @Operation(
            summary = "Get products by price range",
            description = "Retrieves products within a specified price range (inclusive)"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Products by price range retrieved successfully",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid price range",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/price-range")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProductsByPriceRange(
            @Parameter(description = "Minimum price", required = true, example = "100.00")
            @RequestParam @Min(value = 0, message = "Minimum price cannot be negative") Double minPrice,
            @Parameter(description = "Maximum price", required = true, example = "1000.00")
            @RequestParam @Min(value = 0, message = "Maximum price cannot be negative") Double maxPrice) {
        List<ProductResponse> response = productService.getProductsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(ApiResponse.success(response, "Products by price range retrieved successfully"));
    }

    @Operation(
            summary = "Update product",
            description = "Updates an existing product's details including name, description, price, category, and stock"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Product updated successfully",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Product not found",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @Parameter(description = "Product unique identifier", required = true, example = "1")
            @PathVariable @Positive(message = "Product ID must be positive") Integer id,
            @Valid @RequestBody UpdateProductRequest request) {
        ProductResponse response = productService.updateProduct(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Product updated successfully"));
    }

    @Operation(
            summary = "Update product price",
            description = "Updates only the price of an existing product"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Product price updated successfully",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Product not found",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PatchMapping("/{id}/price")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProductPrice(
            @Parameter(description = "Product unique identifier", required = true, example = "1")
            @PathVariable Integer id,
            @Parameter(description = "New price for the product", required = true, example = "899.99")
            @RequestParam Double newPrice) {
        ProductResponse response = productService.updateProductPrice(id, newPrice);
        return ResponseEntity.ok(ApiResponse.success(response, "Product price updated successfully"));
    }

    @Operation(
            summary = "Activate product",
            description = "Activates a product making it available for sale"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Product activated successfully",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Product not found",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<ProductResponse>> activateProduct(
            @Parameter(description = "Product unique identifier", required = true, example = "1")
            @PathVariable Integer id) {
        ProductResponse response = productService.activateProduct(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Product activated successfully"));
    }

    @Operation(
            summary = "Deactivate product",
            description = "Deactivates a product removing it from active listings"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Product deactivated successfully",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Product not found",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<ProductResponse>> deactivateProduct(
            @Parameter(description = "Product unique identifier", required = true, example = "1")
            @PathVariable Integer id) {
        ProductResponse response = productService.deactivateProduct(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Product deactivated successfully"));
    }

    @Operation(
            summary = "Delete product",
            description = "Permanently deletes a product from the system"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Product deleted successfully",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Product not found",
                    content = @Content(mediaType = "application/json")
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @Parameter(description = "Product unique identifier", required = true, example = "1")
            @PathVariable Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Product deleted successfully"));
    }

    @Operation(
            summary = "Get total product count",
            description = "Returns the total number of products in the system"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Total product count retrieved successfully",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> getTotalProductCount() {
        long count = productService.getTotalProductCount();
        return ResponseEntity.ok(ApiResponse.success(count, "Total product count retrieved successfully"));
    }

    @Operation(
            summary = "Get product count by category",
            description = "Returns the number of products in a specific category"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Product count by category retrieved successfully",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Category not found",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/count/category/{categoryId}")
    public ResponseEntity<ApiResponse<Long>> getProductCountByCategory(
            @Parameter(description = "Category unique identifier", required = true, example = "1")
            @PathVariable Integer categoryId) {
        long count = productService.getProductCountByCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success(count, "Product count by category retrieved successfully"));
    }
    
    @Operation(
            summary = "Get products with pagination",
            description = "Retrieves products with pagination support, allowing page number, size, and sorting configuration"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Products retrieved with pagination",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid pagination parameters",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/paginated")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getProductsPaginated(
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "Page number cannot be negative") int page,
            @Parameter(description = "Page size (number of items per page)", example = "10")
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "Page size must be at least 1") @Max(value = 100, message = "Page size cannot exceed 100") int size,
            @Parameter(description = "Field to sort by", example = "product_id")
            @RequestParam(defaultValue = "product_id") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "ASC")
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        Pageable pageable = Pageable.of(page, size);
        Page<ProductResponse> response = productService.getProductsPaginated(pageable, sortBy, sortDirection);
        return ResponseEntity.ok(ApiResponse.success(response, "Products retrieved with pagination"));
    }
    
    @Operation(
            summary = "Search products with pagination",
            description = "Searches for products by name keyword with pagination support"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Product search completed with pagination",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid search parameters",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/search/paginated")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> searchProductsPaginated(
            @Parameter(description = "Search term for product name", required = true, example = "Laptop")
            @RequestParam String term,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "Page number cannot be negative") int page,
            @Parameter(description = "Page size (number of items per page)", example = "10")
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "Page size must be at least 1") @Max(value = 100, message = "Page size cannot exceed 100") int size) {
        Pageable pageable = Pageable.of(page, size);
        Page<ProductResponse> response = productService.searchProductsPaginated(term, pageable);
        return ResponseEntity.ok(ApiResponse.success(response, "Product search completed with pagination"));
    }
    
    @Operation(
            summary = "Get products with advanced filters",
            description = "Retrieves products with comprehensive filtering options including price range, category, stock status, search term, and pagination with sorting"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Filtered products retrieved successfully",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid filter parameters",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getProductsWithFilters(
            @Parameter(description = "Minimum price filter", example = "100.00")
            @RequestParam(required = false) Double minPrice,
            @Parameter(description = "Maximum price filter", example = "1000.00")
            @RequestParam(required = false) Double maxPrice,
            @Parameter(description = "Category ID filter", example = "1")
            @RequestParam(required = false) Integer categoryId,
            @Parameter(description = "Search term for product name or description", example = "Laptop")
            @RequestParam(required = false) String searchTerm,
            @Parameter(description = "Filter by stock availability", example = "true")
            @RequestParam(required = false) Boolean inStock,
            @Parameter(description = "Minimum stock quantity", example = "10")
            @RequestParam(required = false) Integer minStock,
            @Parameter(description = "Maximum stock quantity", example = "100")
            @RequestParam(required = false) Integer maxStock,
            @Parameter(description = "Filter by active status", example = "true")
            @RequestParam(required = false) Boolean isActive,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Field to sort by", example = "product_id")
            @RequestParam(defaultValue = "product_id") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "ASC")
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        ProductFilter filter = new ProductFilter();
        filter.setMinPrice(minPrice);
        filter.setMaxPrice(maxPrice);
        filter.setCategoryId(categoryId);
        filter.setSearchTerm(searchTerm);
        filter.setInStock(inStock);
        filter.setMinStock(minStock);
        filter.setMaxStock(maxStock);
        filter.setIsActive(isActive);
        
        Pageable pageable = Pageable.of(page, size);
        Page<ProductResponse> response = productService.getProductsWithFilters(filter, pageable, sortBy, sortDirection);
        return ResponseEntity.ok(ApiResponse.success(response, "Filtered products retrieved successfully"));
    }
    
    @Operation(
            summary = "Get products sorted with QuickSort algorithm",
            description = "Retrieves products sorted using the QuickSort algorithm with O(n log n) average time complexity"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Products sorted with QuickSort algorithm",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/sorted/quicksort")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProductsSortedWithQuickSort(
            @Parameter(description = "Field to sort by", example = "product_id")
            @RequestParam(defaultValue = "product_id") String sortBy,
            @Parameter(description = "Sort in ascending order", example = "true")
            @RequestParam(defaultValue = "true") boolean ascending) {
        List<ProductResponse> response = productService.getProductsSortedWithQuickSort(sortBy, ascending);
        return ResponseEntity.ok(ApiResponse.success(response, "Products sorted with QuickSort algorithm"));
    }
    
    @Operation(
            summary = "Get products sorted with MergeSort algorithm",
            description = "Retrieves products sorted using the MergeSort algorithm with O(n log n) guaranteed time complexity"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Products sorted with MergeSort algorithm",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/sorted/mergesort")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProductsSortedWithMergeSort(
            @Parameter(description = "Field to sort by", example = "product_id")
            @RequestParam(defaultValue = "product_id") String sortBy,
            @Parameter(description = "Sort in ascending order", example = "true")
            @RequestParam(defaultValue = "true") boolean ascending) {
        List<ProductResponse> response = productService.getProductsSortedWithMergeSort(sortBy, ascending);
        return ResponseEntity.ok(ApiResponse.success(response, "Products sorted with MergeSort algorithm"));
    }
    
    @Operation(
            summary = "Search product by ID using Binary Search algorithm",
            description = "Searches for a product by its ID using the Binary Search algorithm with O(log n) time complexity"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Product found using Binary Search algorithm",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Product not found",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/{id}/binary-search")
    public ResponseEntity<ApiResponse<ProductResponse>> searchProductByIdWithBinarySearch(
            @Parameter(description = "Product unique identifier", required = true, example = "1")
            @PathVariable Integer id) {
        ProductResponse response = productService.searchProductByIdWithBinarySearch(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Product found using Binary Search algorithm"));
    }
}

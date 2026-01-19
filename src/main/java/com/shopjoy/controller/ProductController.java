package com.shopjoy.controller;

import com.shopjoy.dto.filter.ProductFilter;
import com.shopjoy.dto.request.CreateProductRequest;
import com.shopjoy.dto.request.UpdateProductRequest;
import com.shopjoy.dto.response.ApiResponse;
import com.shopjoy.dto.response.ProductResponse;
import com.shopjoy.service.ProductService;
import com.shopjoy.util.Page;
import com.shopjoy.util.Pageable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Product management with comprehensive validation.
 * Base path: /api/v1/products
 * 
 * @Validated enables method-level validation for @PathVariable and @RequestParam
 */
@Validated
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody CreateProductRequest request) {
        ProductResponse response = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Product created successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(
            @PathVariable @Positive(message = "Product ID must be positive") Integer id) {
        ProductResponse response = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Product retrieved successfully"));
    }

    /**
     * Get all products.
     * GET /api/v1/products
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts() {
        List<ProductResponse> response = productService.getAllProducts();
        return ResponseEntity.ok(ApiResponse.success(response, "Products retrieved successfully"));
    }

    /**
     * Get all active products.
     * GET /api/v1/products/active
     */
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getActiveProducts() {
        List<ProductResponse> response = productService.getActiveProducts();
        return ResponseEntity.ok(ApiResponse.success(response, "Active products retrieved successfully"));
    }

    /**
     * Get products by category.
     * GET /api/v1/products/category/{categoryId}
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProductsByCategory(
            @PathVariable Integer categoryId) {
        List<ProductResponse> response = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success(response, "Products by category retrieved successfully"));
    }

    /**
     * Search products by name.
     * GET /api/v1/products/search?name={keyword}
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> searchProductsByName(@RequestParam String name) {
        List<ProductResponse> response = productService.searchProductsByName(name);
        return ResponseEntity.ok(ApiResponse.success(response, "Products search completed successfully"));
    }

    /**
     * Get products by price range.
     * GET /api/v1/products/price-range?minPrice={min}&maxPrice={max}
     */
    @GetMapping("/price-range")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProductsByPriceRange(
            @RequestParam @Min(value = 0, message = "Minimum price cannot be negative") Double minPrice,
            @RequestParam @Min(value = 0, message = "Maximum price cannot be negative") Double maxPrice) {
        List<ProductResponse> response = productService.getProductsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(ApiResponse.success(response, "Products by price range retrieved successfully"));
    }

    /**
     * Update product.
     * PUT /api/v1/products/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable @Positive(message = "Product ID must be positive") Integer id,
            @Valid @RequestBody UpdateProductRequest request) {
        ProductResponse response = productService.updateProduct(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Product updated successfully"));
    }

    /**
     * Update product price.
     * PATCH /api/v1/products/{id}/price?newPrice={price}
     */
    @PatchMapping("/{id}/price")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProductPrice(
            @PathVariable Integer id,
            @RequestParam Double newPrice) {
        ProductResponse response = productService.updateProductPrice(id, newPrice);
        return ResponseEntity.ok(ApiResponse.success(response, "Product price updated successfully"));
    }

    /**
     * Activate product.
     * PATCH /api/v1/products/{id}/activate
     */
    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<ProductResponse>> activateProduct(@PathVariable Integer id) {
        ProductResponse response = productService.activateProduct(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Product activated successfully"));
    }

    /**
     * Deactivate product.
     * PATCH /api/v1/products/{id}/deactivate
     */
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<ProductResponse>> deactivateProduct(@PathVariable Integer id) {
        ProductResponse response = productService.deactivateProduct(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Product deactivated successfully"));
    }

    /**
     * Delete product.
     * DELETE /api/v1/products/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Product deleted successfully"));
    }

    /**
     * Get total product count.
     * GET /api/v1/products/count
     */
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> getTotalProductCount() {
        long count = productService.getTotalProductCount();
        return ResponseEntity.ok(ApiResponse.success(count, "Total product count retrieved successfully"));
    }

    /**
     * Get product count by category.
     * GET /api/v1/products/count/category/{categoryId}
     */
    @GetMapping("/count/category/{categoryId}")
    public ResponseEntity<ApiResponse<Long>> getProductCountByCategory(@PathVariable Integer categoryId) {
        long count = productService.getProductCountByCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success(count, "Product count by category retrieved successfully"));
    }
    
    @GetMapping("/paginated")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getProductsPaginated(
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "Page number cannot be negative") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "Page size must be at least 1") @Max(value = 100, message = "Page size cannot exceed 100") int size,
            @RequestParam(defaultValue = "product_id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        Pageable pageable = Pageable.of(page, size);
        Page<ProductResponse> response = productService.getProductsPaginated(pageable, sortBy, sortDirection);
        return ResponseEntity.ok(ApiResponse.success(response, "Products retrieved with pagination"));
    }
    
    @GetMapping("/search/paginated")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> searchProductsPaginated(
            @RequestParam String term,
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "Page number cannot be negative") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "Page size must be at least 1") @Max(value = 100, message = "Page size cannot exceed 100") int size) {
        Pageable pageable = Pageable.of(page, size);
        Page<ProductResponse> response = productService.searchProductsPaginated(term, pageable);
        return ResponseEntity.ok(ApiResponse.success(response, "Product search completed with pagination"));
    }
    
    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getProductsWithFilters(
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) Boolean inStock,
            @RequestParam(required = false) Integer minStock,
            @RequestParam(required = false) Integer maxStock,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "product_id") String sortBy,
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
    
    @GetMapping("/sorted/quicksort")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProductsSortedWithQuickSort(
            @RequestParam(defaultValue = "product_id") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending) {
        List<ProductResponse> response = productService.getProductsSortedWithQuickSort(sortBy, ascending);
        return ResponseEntity.ok(ApiResponse.success(response, "Products sorted with QuickSort algorithm"));
    }
    
    @GetMapping("/sorted/mergesort")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProductsSortedWithMergeSort(
            @RequestParam(defaultValue = "product_id") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending) {
        List<ProductResponse> response = productService.getProductsSortedWithMergeSort(sortBy, ascending);
        return ResponseEntity.ok(ApiResponse.success(response, "Products sorted with MergeSort algorithm"));
    }
    
    @GetMapping("/{id}/binary-search")
    public ResponseEntity<ApiResponse<ProductResponse>> searchProductByIdWithBinarySearch(@PathVariable Integer id) {
        ProductResponse response = productService.searchProductByIdWithBinarySearch(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Product found using Binary Search algorithm"));
    }
}

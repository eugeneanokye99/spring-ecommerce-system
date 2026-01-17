package com.shopjoy.controller;

import com.shopjoy.dto.request.CreateProductRequest;
import com.shopjoy.dto.request.UpdateProductRequest;
import com.shopjoy.dto.response.ApiResponse;
import com.shopjoy.dto.response.ProductResponse;
import com.shopjoy.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Product management.
 * Base path: /api/v1/products
 * THIN CONTROLLER: Only handles HTTP concerns. All business logic and DTO↔Entity mapping done by services.
 */
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Create a new product.
     * POST /api/v1/products
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody CreateProductRequest request) {
        ProductResponse response = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Product created successfully"));
    }

    /**
     * Get product by ID.
     * GET /api/v1/products/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable Integer id) {
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
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice) {
        List<ProductResponse> response = productService.getProductsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(ApiResponse.success(response, "Products by price range retrieved successfully"));
    }

    /**
     * Update product.
     * PUT /api/v1/products/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Integer id,
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
}

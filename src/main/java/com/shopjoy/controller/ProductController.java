package com.shopjoy.controller;

import com.shopjoy.dto.mapper.ProductMapper;
import com.shopjoy.dto.request.CreateProductRequest;
import com.shopjoy.dto.request.UpdateProductRequest;
import com.shopjoy.dto.response.ApiResponse;
import com.shopjoy.dto.response.ProductResponse;
import com.shopjoy.entity.Product;
import com.shopjoy.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@Valid @RequestBody CreateProductRequest request) {
        Product product = ProductMapper.toProduct(request);
        Product createdProduct = productService.createProduct(product);
        ProductResponse response = ProductMapper.toProductResponse(createdProduct);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Product created successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable Integer id) {
        Product product = productService.getProductById(id);
        ProductResponse response = ProductMapper.toProductResponse(product);
        return ResponseEntity.ok(ApiResponse.success(response, "Product retrieved successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductResponse> responses = products.stream()
                .map(ProductMapper::toProductResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responses, "Products retrieved successfully"));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getActiveProducts() {
        List<Product> products = productService.getActiveProducts();
        List<ProductResponse> responses = products.stream()
                .map(ProductMapper::toProductResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responses, "Active products retrieved successfully"));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProductsByCategory(@PathVariable Integer categoryId) {
        List<Product> products = productService.getProductsByCategory(categoryId);
        List<ProductResponse> responses = products.stream()
                .map(ProductMapper::toProductResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responses, "Products by category retrieved successfully"));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> searchProductsByName(@RequestParam String name) {
        List<Product> products = productService.searchProductsByName(name);
        List<ProductResponse> responses = products.stream()
                .map(ProductMapper::toProductResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responses, "Products search completed successfully"));
    }

    @GetMapping("/price-range")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProductsByPriceRange(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice) {
        List<Product> products = productService.getProductsByPriceRange(minPrice, maxPrice);
        List<ProductResponse> responses = products.stream()
                .map(ProductMapper::toProductResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responses, "Products by price range retrieved successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateProductRequest request) {
        Product product = productService.getProductById(id);
        ProductMapper.updateProductFromRequest(product, request);
        Product updatedProduct = productService.updateProduct(product);
        ProductResponse response = ProductMapper.toProductResponse(updatedProduct);
        return ResponseEntity.ok(ApiResponse.success(response, "Product updated successfully"));
    }

    @PatchMapping("/{id}/price")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProductPrice(
            @PathVariable Integer id,
            @RequestParam Double newPrice) {
        Product updatedProduct = productService.updateProductPrice(id, newPrice);
        ProductResponse response = ProductMapper.toProductResponse(updatedProduct);
        return ResponseEntity.ok(ApiResponse.success(response, "Product price updated successfully"));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<Void>> activateProduct(@PathVariable Integer id) {
        productService.activateProduct(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Product activated successfully"));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<Void>> deactivateProduct(@PathVariable Integer id) {
        productService.deactivateProduct(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Product deactivated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Product deleted successfully"));
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> getTotalProductCount() {
        long count = productService.getTotalProductCount();
        return ResponseEntity.ok(ApiResponse.success(count, "Total product count retrieved successfully"));
    }

    @GetMapping("/count/category/{categoryId}")
    public ResponseEntity<ApiResponse<Long>> getProductCountByCategory(@PathVariable Integer categoryId) {
        long count = productService.getProductCountByCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success(count, "Product count by category retrieved successfully"));
    }
}

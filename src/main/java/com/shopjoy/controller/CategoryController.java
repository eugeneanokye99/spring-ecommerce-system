package com.shopjoy.controller;

import com.shopjoy.dto.request.CreateCategoryRequest;
import com.shopjoy.dto.request.UpdateCategoryRequest;
import com.shopjoy.dto.response.ApiResponse;
import com.shopjoy.dto.response.CategoryResponse;
import com.shopjoy.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Category management.
 * Base path: /api/v1/categories
 * THIN CONTROLLER: Only handles HTTP concerns. All business logic and DTO↔Entity mapping done by services.
 */
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @Valid @RequestBody CreateCategoryRequest request) {
        CategoryResponse response = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Category created successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(@PathVariable Integer id) {
        CategoryResponse response = categoryService.getCategoryById(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Category retrieved successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {
        List<CategoryResponse> response = categoryService.getAllCategories();
        return ResponseEntity.ok(ApiResponse.success(response, "Categories retrieved successfully"));
    }

    @GetMapping("/top-level")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getTopLevelCategories() {
        List<CategoryResponse> response = categoryService.getTopLevelCategories();
        return ResponseEntity.ok(ApiResponse.success(response, "Top level categories retrieved successfully"));
    }

    @GetMapping("/{parentId}/subcategories")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getSubcategories(@PathVariable Integer parentId) {
        List<CategoryResponse> response = categoryService.getSubcategories(parentId);
        return ResponseEntity.ok(ApiResponse.success(response, "Subcategories retrieved successfully"));
    }

    @GetMapping("/{id}/has-subcategories")
    public ResponseEntity<ApiResponse<Boolean>> hasSubcategories(@PathVariable Integer id) {
        boolean hasSubcategories = categoryService.hasSubcategories(id);
        return ResponseEntity.ok(ApiResponse.success(hasSubcategories, "Subcategories check completed"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateCategoryRequest request) {
        CategoryResponse response = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Category updated successfully"));
    }

    @PatchMapping("/{id}/move")
    public ResponseEntity<ApiResponse<CategoryResponse>> moveCategory(
            @PathVariable Integer id,
            @RequestParam(required = false) Integer newParentId) {
        CategoryResponse response = categoryService.moveCategory(id, newParentId);
        return ResponseEntity.ok(ApiResponse.success(response, "Category moved successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Category deleted successfully"));
    }
}

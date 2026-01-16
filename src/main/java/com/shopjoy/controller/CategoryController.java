package com.shopjoy.controller;

import com.shopjoy.dto.mapper.CategoryMapper;
import com.shopjoy.dto.request.CreateCategoryRequest;
import com.shopjoy.dto.request.UpdateCategoryRequest;
import com.shopjoy.dto.response.ApiResponse;
import com.shopjoy.dto.response.CategoryResponse;
import com.shopjoy.entity.Category;
import com.shopjoy.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        Category category = CategoryMapper.toCategory(request);
        Category createdCategory = categoryService.createCategory(category);
        CategoryResponse response = CategoryMapper.toCategoryResponse(createdCategory);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Category created successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(@PathVariable Integer id) {
        Category category = categoryService.getCategoryById(id);
        CategoryResponse response = CategoryMapper.toCategoryResponse(category);
        return ResponseEntity.ok(ApiResponse.success(response, "Category retrieved successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        List<CategoryResponse> responses = categories.stream()
                .map(CategoryMapper::toCategoryResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responses, "Categories retrieved successfully"));
    }

    @GetMapping("/top-level")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getTopLevelCategories() {
        List<Category> categories = categoryService.getTopLevelCategories();
        List<CategoryResponse> responses = categories.stream()
                .map(CategoryMapper::toCategoryResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responses, "Top level categories retrieved successfully"));
    }

    @GetMapping("/{parentId}/subcategories")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getSubcategories(@PathVariable Integer parentId) {
        List<Category> categories = categoryService.getSubcategories(parentId);
        List<CategoryResponse> responses = categories.stream()
                .map(CategoryMapper::toCategoryResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responses, "Subcategories retrieved successfully"));
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
        Category category = categoryService.getCategoryById(id);
        CategoryMapper.updateCategoryFromRequest(category, request);
        Category updatedCategory = categoryService.updateCategory(category);
        CategoryResponse response = CategoryMapper.toCategoryResponse(updatedCategory);
        return ResponseEntity.ok(ApiResponse.success(response, "Category updated successfully"));
    }

    @PatchMapping("/{id}/move")
    public ResponseEntity<ApiResponse<Void>> moveCategory(
            @PathVariable Integer id,
            @RequestParam(required = false) Integer newParentId) {
        categoryService.moveCategory(id, newParentId);
        return ResponseEntity.ok(ApiResponse.success(null, "Category moved successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Category deleted successfully"));
    }
}

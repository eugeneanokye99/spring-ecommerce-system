package com.shopjoy.controller;

import com.shopjoy.dto.request.CreateCategoryRequest;
import com.shopjoy.dto.request.UpdateCategoryRequest;
import com.shopjoy.dto.response.ApiResponse;
import com.shopjoy.dto.response.CategoryResponse;
import com.shopjoy.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type Category controller.
 */
@Tag(name = "Category Management", description = "APIs for managing product categories including hierarchical structure with parent-child relationships")
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Instantiates a new Category controller.
     *
     * @param categoryService the category service
     */
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Create category response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @Operation(
            summary = "Create a new category",
            description = "Creates a new product category with optional parent category for hierarchical structure"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Category created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CategoryResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "Category with this name already exists",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @Valid @RequestBody CreateCategoryRequest request) {
        CategoryResponse response = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Category created successfully"));
    }

    /**
     * Gets category by id.
     *
     * @param id the id
     * @return the category by id
     */
    @Operation(
            summary = "Get category by ID",
            description = "Retrieves a category's details by its unique identifier"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Category retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CategoryResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Category not found",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(
            @Parameter(description = "Category unique identifier", required = true, example = "1")
            @PathVariable Integer id) {
        CategoryResponse response = categoryService.getCategoryById(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Category retrieved successfully"));
    }

    /**
     * Gets all categories.
     *
     * @return the all categories
     */
    @Operation(
            summary = "Get all categories",
            description = "Retrieves a complete list of all categories in the system"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Categories retrieved successfully",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {
        List<CategoryResponse> response = categoryService.getAllCategories();
        return ResponseEntity.ok(ApiResponse.success(response, "Categories retrieved successfully"));
    }

    /**
     * Gets top level categories.
     *
     * @return the top level categories
     */
    @Operation(
            summary = "Get top-level categories",
            description = "Retrieves all categories that have no parent (root categories)"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Top level categories retrieved successfully",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/top-level")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getTopLevelCategories() {
        List<CategoryResponse> response = categoryService.getTopLevelCategories();
        return ResponseEntity.ok(ApiResponse.success(response, "Top level categories retrieved successfully"));
    }

    /**
     * Gets subcategories.
     *
     * @param parentId the parent id
     * @return the subcategories
     */
    @Operation(
            summary = "Get subcategories",
            description = "Retrieves all direct child categories of a parent category"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Subcategories retrieved successfully",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Parent category not found",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/{parentId}/subcategories")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getSubcategories(
            @Parameter(description = "Parent category unique identifier", required = true, example = "1")
            @PathVariable Integer parentId) {
        List<CategoryResponse> response = categoryService.getSubcategories(parentId);
        return ResponseEntity.ok(ApiResponse.success(response, "Subcategories retrieved successfully"));
    }

    /**
     * Has subcategories response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @Operation(
            summary = "Check if category has subcategories",
            description = "Checks whether a category has any child categories"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Subcategories check completed",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Category not found",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/{id}/has-subcategories")
    public ResponseEntity<ApiResponse<Boolean>> hasSubcategories(
            @Parameter(description = "Category unique identifier", required = true, example = "1")
            @PathVariable Integer id) {
        boolean hasSubcategories = categoryService.hasSubcategories(id);
        return ResponseEntity.ok(ApiResponse.success(hasSubcategories, "Subcategories check completed"));
    }

    /**
     * Update category response entity.
     *
     * @param id      the id
     * @param request the request
     * @return the response entity
     */
    @Operation(
            summary = "Update category",
            description = "Updates an existing category's details including name and description"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Category updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CategoryResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Category not found",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @Parameter(description = "Category unique identifier", required = true, example = "1")
            @PathVariable Integer id,
            @Valid @RequestBody UpdateCategoryRequest request) {
        CategoryResponse response = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Category updated successfully"));
    }

    /**
     * Move category response entity.
     *
     * @param id          the id
     * @param newParentId the new parent id
     * @return the response entity
     */
    @Operation(
            summary = "Move category",
            description = "Moves a category to a new parent category or to top level (null parent)"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Category moved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CategoryResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Category not found",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid move operation (e.g., circular reference)",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PatchMapping("/{id}/move")
    public ResponseEntity<ApiResponse<CategoryResponse>> moveCategory(
            @Parameter(description = "Category unique identifier", required = true, example = "1")
            @PathVariable Integer id,
            @Parameter(description = "New parent category ID (null for top level)", example = "2")
            @RequestParam(required = false) Integer newParentId) {
        CategoryResponse response = categoryService.moveCategory(id, newParentId);
        return ResponseEntity.ok(ApiResponse.success(response, "Category moved successfully"));
    }

    /**
     * Delete category response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @Operation(
            summary = "Delete category",
            description = "Permanently deletes a category from the system (fails if it has subcategories or products)"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Category deleted successfully",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Category not found",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Cannot delete category with subcategories or products",
                    content = @Content(mediaType = "application/json")
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(
            @Parameter(description = "Category unique identifier", required = true, example = "1")
            @PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Category deleted successfully"));
    }
}

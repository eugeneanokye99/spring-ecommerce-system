package com.shopjoy.service;

import com.shopjoy.dto.request.CreateCategoryRequest;
import com.shopjoy.dto.request.UpdateCategoryRequest;
import com.shopjoy.dto.response.CategoryResponse;

import java.util.List;

/**
 * Service interface for Category management operations.
 * DTO-FIRST DESIGN: All methods accept and return DTOs, not entities.
 * Service layer handles all DTO ↔ Entity mapping internally.
 */
public interface CategoryService {
    
    CategoryResponse createCategory(CreateCategoryRequest request);
    
    CategoryResponse getCategoryById(Integer categoryId);
    
    List<CategoryResponse> getAllCategories();
    
    List<CategoryResponse> getTopLevelCategories();
    
    List<CategoryResponse> getSubcategories(Integer parentCategoryId);
    
    boolean hasSubcategories(Integer categoryId);
    
    CategoryResponse updateCategory(Integer categoryId, UpdateCategoryRequest request);
    
    void deleteCategory(Integer categoryId);
    
    CategoryResponse moveCategory(Integer categoryId, Integer newParentId);
}

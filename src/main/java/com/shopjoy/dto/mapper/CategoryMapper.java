package com.shopjoy.dto.mapper;

import com.shopjoy.dto.request.CreateCategoryRequest;
import com.shopjoy.dto.request.UpdateCategoryRequest;
import com.shopjoy.dto.response.CategoryResponse;
import com.shopjoy.entity.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper for Category entity and DTOs.
 */
public class CategoryMapper {
    
    public static Category toCategory(CreateCategoryRequest request) {
        if (request == null) {
            return null;
        }
        
        Category category = new Category();
        category.setCategoryName(request.getCategoryName());
        category.setDescription(request.getDescription());
        category.setParentCategoryId(request.getParentCategoryId());
        
        return category;
    }
    
    public static CategoryResponse toCategoryResponse(Category category) {
        if (category == null) {
            return null;
        }
        
        return new CategoryResponse(
            category.getCategoryId(),
            category.getCategoryName(),
            category.getDescription(),
            category.getParentCategoryId(),
            category.getCreatedAt()
        );
    }
    
    public static List<CategoryResponse> toCategoryResponseList(List<Category> categories) {
        if (categories == null) {
            return null;
        }
        
        List<CategoryResponse> responses = new ArrayList<>();
        for (Category category : categories) {
            responses.add(toCategoryResponse(category));
        }
        return responses;
    }
    
    public static void updateCategoryFromRequest(Category category, UpdateCategoryRequest request) {
        if (category == null || request == null) {
            return;
        }
        
        if (request.getCategoryName() != null) {
            category.setCategoryName(request.getCategoryName());
        }
        
        if (request.getDescription() != null) {
            category.setDescription(request.getDescription());
        }
    }
}

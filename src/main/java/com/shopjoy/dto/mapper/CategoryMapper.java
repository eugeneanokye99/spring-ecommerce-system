package com.shopjoy.dto.mapper;

import com.shopjoy.dto.request.CreateCategoryRequest;
import com.shopjoy.dto.request.UpdateCategoryRequest;
import com.shopjoy.dto.response.CategoryResponse;
import com.shopjoy.entity.Category;


/**
 * Mapper for Category entity and DTOs.
 */
public class CategoryMapper {

    /**
     * To category category.
     *
     * @param request the request
     * @return the category
     */
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

    /**
     * To category response category response.
     *
     * @param category the category
     * @return the category response
     */
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


    /**
     * Update category from request.
     *
     * @param category the category
     * @param request  the request
     */
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

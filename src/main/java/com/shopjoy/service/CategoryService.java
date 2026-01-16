package com.shopjoy.service;

import com.shopjoy.entity.Category;
import com.shopjoy.exception.DuplicateResourceException;
import com.shopjoy.exception.ResourceNotFoundException;
import com.shopjoy.exception.ValidationException;

import java.util.List;

/**
 * Service interface for Category management operations.
 * Handles category hierarchy and product categorization.
 */
public interface CategoryService {
    
    /**
     * Creates a new category.
     * Validates category data and parent category existence if applicable.
     * 
     * @param category the category to create
     * @return the created category with generated ID
     * @throws ValidationException if category data is invalid
     * @throws ResourceNotFoundException if parent category not found
     * @throws DuplicateResourceException if category name already exists
     */
    Category createCategory(Category category);
    
    /**
     * Retrieves a category by its ID.
     * 
     * @param categoryId the category ID
     * @return the category
     * @throws ResourceNotFoundException if category not found
     */
    Category getCategoryById(Integer categoryId);
    
    /**
     * Retrieves all categories.
     * 
     * @return list of all categories
     */
    List<Category> getAllCategories();
    
    /**
     * Retrieves all top-level categories (no parent).
     * 
     * @return list of top-level categories
     */
    List<Category> getTopLevelCategories();
    
    /**
     * Retrieves all subcategories of a parent category.
     * 
     * @param parentCategoryId the parent category ID
     * @return list of subcategories
     */
    List<Category> getSubcategories(Integer parentCategoryId);
    
    /**
     * Checks if a category has subcategories.
     * 
     * @param categoryId the category ID
     * @return true if category has subcategories, false otherwise
     */
    boolean hasSubcategories(Integer categoryId);
    
    /**
     * Updates an existing category.
     * Validates that moving to a new parent doesn't create circular references.
     * 
     * @param category the category with updated information
     * @return the updated category
     * @throws ResourceNotFoundException if category not found
     * @throws ValidationException if category data is invalid
     * @throws BusinessException if update would create circular reference
     */
    Category updateCategory(Category category);
    
    /**
     * Deletes a category.
     * Cannot delete if category has products or subcategories.
     * 
     * @param categoryId the category ID
     * @throws ResourceNotFoundException if category not found
     * @throws BusinessException if category has products or subcategories
     */
    void deleteCategory(Integer categoryId);
    
    /**
     * Moves a category to a different parent.
     * 
     * @param categoryId the category to move
     * @param newParentId the new parent category ID (null for top-level)
     * @return the updated category
     * @throws ResourceNotFoundException if category or parent not found
     * @throws BusinessException if move would create circular reference
     */
    Category moveCategory(Integer categoryId, Integer newParentId);
}

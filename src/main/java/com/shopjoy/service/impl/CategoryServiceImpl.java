package com.shopjoy.service.impl;

import com.shopjoy.entity.Category;
import com.shopjoy.exception.BusinessException;
import com.shopjoy.exception.ResourceNotFoundException;
import com.shopjoy.exception.ValidationException;
import com.shopjoy.repository.CategoryRepository;
import com.shopjoy.service.CategoryService;
import com.shopjoy.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The type Category service.
 */
@Service
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    
    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    
    private final CategoryRepository categoryRepository;
    private final ProductService productService;

    /**
     * Instantiates a new Category service.
     *
     * @param categoryRepository the category repository
     * @param productService     the product service
     */
    public CategoryServiceImpl(CategoryRepository categoryRepository, ProductService productService) {
        this.categoryRepository = categoryRepository;
        this.productService = productService;
    }
    
    @Override
    @Transactional()
    public Category createCategory(Category category) {
        logger.info("Creating new category: {}", category.getCategoryName());
        
        validateCategoryData(category);
        
        if (category.getParentCategoryId() != null) {
            categoryRepository.findById(category.getParentCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", category.getParentCategoryId()));
        }
        
        category.setCreatedAt(LocalDateTime.now());
        Category createdCategory = categoryRepository.save(category);
        
        logger.info("Successfully created category with ID: {}", createdCategory.getCategoryId());
        return createdCategory;
    }
    
    @Override
    public Category getCategoryById(Integer categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
    }
    
    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    
    @Override
    public List<Category> getTopLevelCategories() {
        return categoryRepository.findTopLevelCategories();
    }
    
    @Override
    public List<Category> getSubcategories(Integer parentCategoryId) {
        if (parentCategoryId == null) {
            throw new ValidationException("Parent category ID cannot be null");
        }
        return categoryRepository.findSubcategories(parentCategoryId);
    }
    
    @Override
    public boolean hasSubcategories(Integer categoryId) {
        if (categoryId == null) {
            throw new ValidationException("Category ID cannot be null");
        }
        return categoryRepository.hasSubcategories(categoryId);
    }
    
    @Override
    @Transactional()
    public Category updateCategory(Category category) {
        logger.info("Updating category ID: {}", category.getCategoryId());
        
        getCategoryById(category.getCategoryId());
        validateCategoryData(category);
        
        if (category.getParentCategoryId() != null) {
            if (category.getParentCategoryId().equals(category.getCategoryId())) {
                throw new BusinessException("Category cannot be its own parent");
            }
            
            if (wouldCreateCircularReference(category.getCategoryId(), category.getParentCategoryId())) {
                throw new BusinessException("Moving category would create circular reference");
            }
        }
        
        Category updatedCategory = categoryRepository.update(category);
        logger.info("Successfully updated category ID: {}", category.getCategoryId());
        
        return updatedCategory;
    }
    
    @Override
    @Transactional()
    public void deleteCategory(Integer categoryId) {
        logger.info("Deleting category ID: {}", categoryId);
        
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category", "id", categoryId);
        }
        
        if (hasSubcategories(categoryId)) {
            throw new BusinessException("Cannot delete category with subcategories");
        }
        
        long productCount = productService.getProductCountByCategory(categoryId);
        if (productCount > 0) {
            throw new BusinessException(
                    String.format("Cannot delete category with %d products", productCount));
        }
        
        categoryRepository.delete(categoryId);
        logger.info("Successfully deleted category ID: {}", categoryId);
    }
    
    @Override
    @Transactional()
    public Category moveCategory(Integer categoryId, Integer newParentId) {
        logger.info("Moving category {} to parent {}", categoryId, newParentId);
        
        Category category = getCategoryById(categoryId);
        
        if (newParentId != null) {
            if (newParentId.equals(categoryId)) {
                throw new BusinessException("Category cannot be its own parent");
            }
            
            categoryRepository.findById(newParentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", newParentId));
            
            if (wouldCreateCircularReference(categoryId, newParentId)) {
                throw new BusinessException("Moving category would create circular reference");
            }
        }
        
        category.setParentCategoryId(newParentId);
        return categoryRepository.update(category);
    }
    
    private boolean wouldCreateCircularReference(Integer categoryId, Integer newParentId) {
        Integer currentId = newParentId;
        while (currentId != null) {
            if (currentId.equals(categoryId)) {
                return true;
            }
            Category parent = categoryRepository.findById(currentId).orElse(null);
            currentId = (parent != null) ? parent.getParentCategoryId() : null;
        }
        return false;
    }
    
    private void validateCategoryData(Category category) {
        if (category == null) {
            throw new ValidationException("Category data cannot be null");
        }
        
        if (category.getCategoryName() == null || category.getCategoryName().trim().isEmpty()) {
            throw new ValidationException("categoryName", "must not be empty");
        }
        
        if (category.getCategoryName().length() > 100) {
            throw new ValidationException("categoryName", "must not exceed 100 characters");
        }
    }
}

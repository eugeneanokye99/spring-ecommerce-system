package com.shopjoy.integration;

import com.shopjoy.dto.request.CreateCategoryRequest;
import com.shopjoy.dto.request.UpdateCategoryRequest;
import com.shopjoy.dto.response.CategoryResponse;
import com.shopjoy.exception.ResourceNotFoundException;
import com.shopjoy.service.CategoryService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for Category management
 * Tests the full stack: Controller -> Service -> Repository -> Database
 */
@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
class CategoryIntegrationTest {

    @Autowired
    private CategoryService categoryService;

    private CreateCategoryRequest testCategoryRequest;

    @BeforeEach
    void setUp() {
        testCategoryRequest = new CreateCategoryRequest();
        testCategoryRequest.setCategoryName("Test Category " + System.currentTimeMillis());
        testCategoryRequest.setDescription("A test category for integration testing");
    }

    @Test
    @Order(1)
    @DisplayName("Should create a new category successfully")
    void testCreateCategory() {
        // Act
        CategoryResponse response = categoryService.createCategory(testCategoryRequest);

        // Assert
        assertNotNull(response);
        assertEquals(testCategoryRequest.getCategoryName(), response.getCategoryName());
        assertEquals(testCategoryRequest.getDescription(), response.getDescription());
        assertNotNull(response.getCreatedAt());
    }

    @Test
    @Order(2)
    @DisplayName("Should retrieve category by ID")
    void testGetCategoryById() {
        // Arrange
        CategoryResponse createdCategory = categoryService.createCategory(testCategoryRequest);

        // Act
        CategoryResponse retrievedCategory = categoryService.getCategoryById(createdCategory.getCategoryId());

        // Assert
        assertNotNull(retrievedCategory);
        assertEquals(createdCategory.getCategoryId(), retrievedCategory.getCategoryId());
        assertEquals(createdCategory.getCategoryName(), retrievedCategory.getCategoryName());
    }

    @Test
    @Order(3)
    @DisplayName("Should throw exception when category not found")
    void testGetCategoryByIdNotFound() {
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.getCategoryById(999999);
        });
    }

    @Test
    @Order(4)
    @DisplayName("Should retrieve all categories")
    void testGetAllCategories() {
        // Arrange
        categoryService.createCategory(testCategoryRequest);

        // Act
        List<CategoryResponse> categories = categoryService.getAllCategories();

        // Assert
        assertNotNull(categories);
        assertFalse(categories.isEmpty());
    }

    @Test
    @Order(5)
    @DisplayName("Should create category with parent")
    void testCreateCategoryWithParent() {
        // Arrange
        CategoryResponse parentCategory = categoryService.createCategory(testCategoryRequest);

        CreateCategoryRequest childRequest = new CreateCategoryRequest();
        childRequest.setCategoryName("Child Category " + System.currentTimeMillis());
        childRequest.setDescription("Child category");
        childRequest.setParentCategoryId(parentCategory.getCategoryId());

        // Act
        CategoryResponse childCategory = categoryService.createCategory(childRequest);

        // Assert
        assertNotNull(childCategory);
        assertEquals(parentCategory.getCategoryId(), childCategory.getParentCategoryId());
    }

    @Test
    @Order(6)
    @DisplayName("Should retrieve top-level categories")
    void testGetTopLevelCategories() {
        // Arrange
        categoryService.createCategory(testCategoryRequest);

        // Act
        List<CategoryResponse> topLevelCategories = categoryService.getTopLevelCategories();

        // Assert
        assertNotNull(topLevelCategories);
        assertFalse(topLevelCategories.isEmpty());
        assertTrue(topLevelCategories.stream().allMatch(c -> c.getParentCategoryId() == null));
    }

    @Test
    @Order(7)
    @DisplayName("Should retrieve subcategories")
    void testGetSubcategories() {
        // Arrange
        CategoryResponse parentCategory = categoryService.createCategory(testCategoryRequest);

        CreateCategoryRequest childRequest = new CreateCategoryRequest();
        childRequest.setCategoryName("Child Category " + System.currentTimeMillis());
        childRequest.setDescription("Child category");
        childRequest.setParentCategoryId(parentCategory.getCategoryId());
        categoryService.createCategory(childRequest);

        // Act
        List<CategoryResponse> subcategories = categoryService.getSubcategories(parentCategory.getCategoryId());

        // Assert
        assertNotNull(subcategories);
        assertFalse(subcategories.isEmpty());
        assertTrue(subcategories.stream()
            .allMatch(c -> c.getParentCategoryId().equals(parentCategory.getCategoryId())));
    }

    @Test
    @Order(8)
    @DisplayName("Should check if category has subcategories")
    void testHasSubcategories() {
        // Arrange
        CategoryResponse parentCategory = categoryService.createCategory(testCategoryRequest);

        CreateCategoryRequest childRequest = new CreateCategoryRequest();
        childRequest.setCategoryName("Child Category " + System.currentTimeMillis());
        childRequest.setParentCategoryId(parentCategory.getCategoryId());
        categoryService.createCategory(childRequest);

        // Act
        boolean hasSubcategories = categoryService.hasSubcategories(parentCategory.getCategoryId());

        // Assert
        assertTrue(hasSubcategories);
    }

    @Test
    @Order(9)
    @DisplayName("Should update category successfully")
    void testUpdateCategory() {
        // Arrange
        CategoryResponse createdCategory = categoryService.createCategory(testCategoryRequest);

        UpdateCategoryRequest updateRequest = new UpdateCategoryRequest();
        updateRequest.setCategoryName("Updated Category Name");
        updateRequest.setDescription("Updated description");

        // Act
        CategoryResponse updatedCategory = categoryService.updateCategory(
            createdCategory.getCategoryId(), 
            updateRequest
        );

        // Assert
        assertNotNull(updatedCategory);
        assertEquals(createdCategory.getCategoryId(), updatedCategory.getCategoryId());
        assertEquals("Updated Category Name", updatedCategory.getCategoryName());
        assertEquals("Updated description", updatedCategory.getDescription());
    }

    @Test
    @Order(10)
    @DisplayName("Should move category to new parent")
    void testMoveCategory() {
        // Arrange
        CategoryResponse oldParent = categoryService.createCategory(testCategoryRequest);

        CreateCategoryRequest newParentRequest = new CreateCategoryRequest();
        newParentRequest.setCategoryName("New Parent " + System.currentTimeMillis());
        CategoryResponse newParent = categoryService.createCategory(newParentRequest);

        CreateCategoryRequest childRequest = new CreateCategoryRequest();
        childRequest.setCategoryName("Child Category " + System.currentTimeMillis());
        childRequest.setParentCategoryId(oldParent.getCategoryId());
        CategoryResponse childCategory = categoryService.createCategory(childRequest);

        // Act
        CategoryResponse movedCategory = categoryService.moveCategory(
            childCategory.getCategoryId(), 
            newParent.getCategoryId()
        );

        // Assert
        assertNotNull(movedCategory);
        assertEquals(newParent.getCategoryId(), movedCategory.getParentCategoryId());
    }

    @Test
    @Order(11)
    @DisplayName("Should delete category successfully")
    void testDeleteCategory() {
        // Arrange
        CategoryResponse createdCategory = categoryService.createCategory(testCategoryRequest);

        // Act
        categoryService.deleteCategory(createdCategory.getCategoryId());

        // Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.getCategoryById(createdCategory.getCategoryId());
        });
    }
}

package com.shopjoy.integration;

import com.shopjoy.dto.request.CreateCategoryRequest;
import com.shopjoy.dto.request.CreateProductRequest;
import com.shopjoy.dto.request.UpdateProductRequest;
import com.shopjoy.dto.response.CategoryResponse;
import com.shopjoy.dto.response.ProductResponse;
import com.shopjoy.exception.ResourceNotFoundException;
import com.shopjoy.service.CategoryService;
import com.shopjoy.service.ProductService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for Product management
 * Tests the full stack: Controller -> Service -> Repository -> Database
 */
@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
class ProductIntegrationTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    private Integer testCategoryId;
    private CreateProductRequest testProductRequest;

    @BeforeEach
    void setUp() {
        // Create a test category first
        CreateCategoryRequest categoryRequest = new CreateCategoryRequest();
        categoryRequest.setCategoryName("Test Category " + System.currentTimeMillis());
        categoryRequest.setDescription("Category for testing products");
        CategoryResponse category = categoryService.createCategory(categoryRequest);
        testCategoryId = category.getCategoryId();

        // Create test product request
        testProductRequest = new CreateProductRequest();
        testProductRequest.setProductName("Test Product " + System.currentTimeMillis());
        testProductRequest.setDescription("A test product for integration testing");
        testProductRequest.setPrice(99.99);
        testProductRequest.setCategoryId(testCategoryId);
        testProductRequest.setImageUrl("https://example.com/image.jpg");
        testProductRequest.setIsActive(true);
    }

    @Test
    @Order(1)
    @DisplayName("Should create a new product successfully")
    void testCreateProduct() {
        // Act
        ProductResponse response = productService.createProduct(testProductRequest);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getProductId());
        assertEquals(testProductRequest.getProductName(), response.getProductName());
        assertEquals(testProductRequest.getDescription(), response.getDescription());
        assertEquals(testProductRequest.getPrice(), response.getPrice());
        assertEquals(testProductRequest.getCategoryId(), response.getCategoryId());
        assertTrue(response.isActive());
        assertNotNull(response.getCreatedAt());
    }

    @Test
    @Order(2)
    @DisplayName("Should retrieve product by ID")
    void testGetProductById() {
        // Arrange
        ProductResponse createdProduct = productService.createProduct(testProductRequest);

        // Act
        ProductResponse retrievedProduct = productService.getProductById(createdProduct.getProductId());

        // Assert
        assertNotNull(retrievedProduct);
        assertEquals(createdProduct.getProductId(), retrievedProduct.getProductId());
        assertEquals(createdProduct.getProductName(), retrievedProduct.getProductName());
    }

    @Test
    @Order(3)
    @DisplayName("Should throw exception when product not found")
    void testGetProductByIdNotFound() {
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            productService.getProductById(999999);
        });
    }

    @Test
    @Order(4)
    @DisplayName("Should retrieve all products")
    void testGetAllProducts() {
        // Arrange
        productService.createProduct(testProductRequest);
        
        CreateProductRequest secondProduct = new CreateProductRequest();
        secondProduct.setProductName("Second Product " + System.currentTimeMillis());
        secondProduct.setDescription("Another test product");
        secondProduct.setPrice(149.99);
        secondProduct.setCategoryId(testCategoryId);
        secondProduct.setIsActive(true);
        productService.createProduct(secondProduct);

        // Act
        List<ProductResponse> products = productService.getAllProducts();

        // Assert
        assertNotNull(products);
        assertTrue(products.size() >= 2);
    }

    @Test
    @Order(5)
    @DisplayName("Should retrieve only active products")
    void testGetActiveProducts() {
        // Arrange
        CreateProductRequest activeProduct = new CreateProductRequest();
        activeProduct.setProductName("Active Product " + System.currentTimeMillis());
        activeProduct.setDescription("Active product");
        activeProduct.setPrice(99.99);
        activeProduct.setCategoryId(testCategoryId);
        activeProduct.setIsActive(true);
        productService.createProduct(activeProduct);

        CreateProductRequest inactiveProduct = new CreateProductRequest();
        inactiveProduct.setProductName("Inactive Product " + System.currentTimeMillis());
        inactiveProduct.setDescription("Inactive product");
        inactiveProduct.setPrice(79.99);
        inactiveProduct.setCategoryId(testCategoryId);
        inactiveProduct.setIsActive(false);
        productService.createProduct(inactiveProduct);

        // Act
        List<ProductResponse> activeProducts = productService.getActiveProducts();

        // Assert
        assertNotNull(activeProducts);
        assertTrue(activeProducts.stream().allMatch(ProductResponse::isActive));
    }

    @Test
    @Order(6)
    @DisplayName("Should retrieve products by category")
    void testGetProductsByCategory() {
        // Arrange
        productService.createProduct(testProductRequest);

        // Act
        List<ProductResponse> products = productService.getProductsByCategory(testCategoryId);

        // Assert
        assertNotNull(products);
        assertFalse(products.isEmpty());
        assertTrue(products.stream().allMatch(p -> p.getCategoryId() == testCategoryId));
    }

    @Test
    @Order(7)
    @DisplayName("Should retrieve products by price range")
    void testGetProductsByPriceRange() {
        // Arrange
        productService.createProduct(testProductRequest);

        // Act
        List<ProductResponse> products = productService.getProductsByPriceRange(50.0, 150.0);

        // Assert
        assertNotNull(products);
        assertFalse(products.isEmpty());
        assertTrue(products.stream().allMatch(p -> p.getPrice() >= 50.0 && p.getPrice() <= 150.0));
    }

    @Test
    @Order(8)
    @DisplayName("Should update product successfully")
    void testUpdateProduct() {
        // Arrange
        ProductResponse createdProduct = productService.createProduct(testProductRequest);

        UpdateProductRequest updateRequest = new UpdateProductRequest();
        updateRequest.setProductName("Updated Product Name");
        updateRequest.setDescription("Updated description");
        updateRequest.setPrice(129.99);
        updateRequest.setIsActive(false);

        // Act
        ProductResponse updatedProduct = productService.updateProduct(
            createdProduct.getProductId(), 
            updateRequest
        );

        // Assert
        assertNotNull(updatedProduct);
        assertEquals(createdProduct.getProductId(), updatedProduct.getProductId());
        assertEquals("Updated Product Name", updatedProduct.getProductName());
        assertEquals("Updated description", updatedProduct.getDescription());
        assertEquals(129.99, updatedProduct.getPrice());
        assertFalse(updatedProduct.isActive());
    }

    @Test
    @Order(9)
    @DisplayName("Should update product price successfully")
    void testUpdateProductPrice() {
        // Arrange
        ProductResponse createdProduct = productService.createProduct(testProductRequest);
        double newPrice = 199.99;

        // Act
        ProductResponse updatedProduct = productService.updateProductPrice(
            createdProduct.getProductId(), 
            newPrice
        );

        // Assert
        assertNotNull(updatedProduct);
        assertEquals(createdProduct.getProductId(), updatedProduct.getProductId());
        assertEquals(newPrice, updatedProduct.getPrice());
    }

    @Test
    @Order(10)
    @DisplayName("Should activate product successfully")
    void testActivateProduct() {
        // Arrange
        testProductRequest.setIsActive(false);
        ProductResponse createdProduct = productService.createProduct(testProductRequest);

        // Act
        productService.activateProduct(createdProduct.getProductId());
        ProductResponse activatedProduct = productService.getProductById(createdProduct.getProductId());

        // Assert
        assertTrue(activatedProduct.isActive());
    }

    @Test
    @Order(11)
    @DisplayName("Should deactivate product successfully")
    void testDeactivateProduct() {
        // Arrange
        testProductRequest.setIsActive(true);
        ProductResponse createdProduct = productService.createProduct(testProductRequest);

        // Act
        productService.deactivateProduct(createdProduct.getProductId());
        ProductResponse deactivatedProduct = productService.getProductById(createdProduct.getProductId());

        // Assert
        assertFalse(deactivatedProduct.isActive());
    }

    @Test
    @Order(12)
    @DisplayName("Should delete product successfully")
    void testDeleteProduct() {
        // Arrange
        ProductResponse createdProduct = productService.createProduct(testProductRequest);

        // Act
        productService.deleteProduct(createdProduct.getProductId());

        // Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            productService.getProductById(createdProduct.getProductId());
        });
    }

    @Test
    @Order(13)
    @DisplayName("Should get total product count")
    void testGetTotalProductCount() {
        // Arrange
        productService.createProduct(testProductRequest);

        // Act
        long count = productService.getTotalProductCount();

        // Assert
        assertTrue(count >= 1);
    }

    @Test
    @Order(14)
    @DisplayName("Should get product count by category")
    void testGetProductCountByCategory() {
        // Arrange
        productService.createProduct(testProductRequest);

        // Act
        long count = productService.getProductCountByCategory(testCategoryId);

        // Assert
        assertTrue(count >= 1);
    }
}

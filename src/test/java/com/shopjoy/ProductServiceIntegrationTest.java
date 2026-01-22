package com.shopjoy;

import com.shopjoy.entity.Product;
import com.shopjoy.repository.ProductRepository;
import com.shopjoy.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ProductServiceIntegrationTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        testProduct = new Product();
        testProduct.setProductName("Test Product");
        testProduct.setDescription("Test Description");
        testProduct.setPrice(BigDecimal.valueOf(99.99));
        testProduct.setCostPrice(BigDecimal.valueOf(50.00));
        testProduct.setStockQuantity(100);
    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
    }

    @Test
    void createProduct_ShouldPersistToDatabase() {
        Product saved = productRepository.save(testProduct);

        assertThat(saved.getProductId()).isNotNull();
        assertThat(saved.getProductName()).isEqualTo("Test Product");
        assertThat(saved.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(99.99));
    }

    @Test
    void findAllProducts_ShouldReturnAllProducts() {
        productRepository.save(testProduct);
        
        Product product2 = new Product();
        product2.setProductName("Second Product");
        product2.setPrice(BigDecimal.valueOf(49.99));
        product2.setStockQuantity(50);
        productRepository.save(product2);

        List<Product> products = productRepository.findAll();

        assertThat(products).hasSize(2);
        assertThat(products).extracting(Product::getProductName)
            .containsExactlyInAnyOrder("Test Product", "Second Product");
    }

    @Test
    void updateProduct_ShouldModifyExistingProduct() {
        Product saved = productRepository.save(testProduct);
        Long productId = saved.getProductId();

        saved.setProductName("Updated Product");
        saved.setPrice(BigDecimal.valueOf(149.99));
        productRepository.save(saved);

        Product updated = productRepository.findById(productId).orElseThrow();

        assertThat(updated.getProductName()).isEqualTo("Updated Product");
        assertThat(updated.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(149.99));
    }

    @Test
    void deleteProduct_ShouldRemoveFromDatabase() {
        Product saved = productRepository.save(testProduct);
        Long productId = saved.getProductId();

        productRepository.deleteById(productId);

        assertThat(productRepository.findById(productId)).isEmpty();
    }

    @Test
    void findProductsByPriceRange_ShouldReturnMatchingProducts() {
        Product product1 = createProduct("Product 1", BigDecimal.valueOf(25.00));
        Product product2 = createProduct("Product 2", BigDecimal.valueOf(75.00));
        Product product3 = createProduct("Product 3", BigDecimal.valueOf(125.00));

        productRepository.saveAll(List.of(product1, product2, product3));

        List<Product> results = productRepository.findByPriceBetween(
            BigDecimal.valueOf(20.00), 
            BigDecimal.valueOf(100.00)
        );

        assertThat(results).hasSize(2);
        assertThat(results).extracting(Product::getProductName)
            .containsExactlyInAnyOrder("Product 1", "Product 2");
    }

    @Test
    void findProductsByStockQuantity_ShouldReturnLowStockProducts() {
        Product product1 = createProduct("Low Stock 1", BigDecimal.valueOf(10.00));
        product1.setStockQuantity(5);
        
        Product product2 = createProduct("High Stock", BigDecimal.valueOf(20.00));
        product2.setStockQuantity(100);
        
        Product product3 = createProduct("Low Stock 2", BigDecimal.valueOf(15.00));
        product3.setStockQuantity(8);

        productRepository.saveAll(List.of(product1, product2, product3));

        List<Product> lowStock = productRepository.findByStockQuantityLessThan(10);

        assertThat(lowStock).hasSize(2);
        assertThat(lowStock).extracting(Product::getProductName)
            .containsExactlyInAnyOrder("Low Stock 1", "Low Stock 2");
    }

    @Test
    void transactionRollback_ShouldNotPersistChanges() {
        Product saved = productRepository.save(testProduct);
        Long productId = saved.getProductId();

        try {
            saved.setProductName("Should Rollback");
            saved.setPrice(BigDecimal.valueOf(-100));
            productRepository.save(saved);
            throw new RuntimeException("Simulated error");
        } catch (Exception e) {
        }

        Product original = productRepository.findById(productId).orElseThrow();
        assertThat(original.getProductName()).isEqualTo("Test Product");
    }

    private Product createProduct(String name, BigDecimal price) {
        Product product = new Product();
        product.setProductName(name);
        product.setDescription("Description for " + name);
        product.setPrice(price);
        product.setCostPrice(price.multiply(BigDecimal.valueOf(0.5)));
        product.setStockQuantity(50);
        return product;
    }
}

package com.shopjoy.performance;

import com.shopjoy.entity.Product;
import com.shopjoy.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class QueryOptimizationTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void testPaginationPerformance() {
        createTestProducts(1000);

        long startTime = System.currentTimeMillis();
        productRepository.findAll(PageRequest.of(0, 20));
        long duration = System.currentTimeMillis() - startTime;

        assertThat(duration).isLessThan(100);
    }

    @Test
    void testIndexedQueryPerformance() {
        createTestProducts(5000);
        entityManager.flush();
        entityManager.clear();

        long startTime = System.currentTimeMillis();
        productRepository.findById(2500L);
        long duration = System.currentTimeMillis() - startTime;

        assertThat(duration).isLessThan(50);
    }

    @Test
    void testPriceRangeQueryWithIndex() {
        createTestProducts(3000);
        entityManager.flush();
        entityManager.clear();

        long startTime = System.currentTimeMillis();
        List<Product> results = productRepository.findByPriceBetween(
            BigDecimal.valueOf(100), 
            BigDecimal.valueOf(500)
        );
        long duration = System.currentTimeMillis() - startTime;

        assertThat(duration).isLessThan(200);
        assertThat(results).isNotEmpty();
    }

    @Test
    void testSortingPerformance() {
        createTestProducts(2000);

        long startTime = System.currentTimeMillis();
        productRepository.findAll(Sort.by(Sort.Direction.ASC, "price"));
        long duration = System.currentTimeMillis() - startTime;

        assertThat(duration).isLessThan(300);
    }

    @Test
    void testBatchInsertPerformance() {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            Product product = new Product();
            product.setProductName("Batch Product " + i);
            product.setPrice(BigDecimal.valueOf(50.00 + i));
            product.setStockQuantity(100);
            products.add(product);
        }

        long startTime = System.currentTimeMillis();
        productRepository.saveAll(products);
        long duration = System.currentTimeMillis() - startTime;

        assertThat(duration).isLessThan(2000);
    }

    @Test
    void testLazyLoadingPerformance() {
        createTestProducts(100);
        entityManager.flush();
        entityManager.clear();

        long startTime = System.currentTimeMillis();
        Product product = productRepository.findById(50L).orElseThrow();
        product.getProductName();
        long duration = System.currentTimeMillis() - startTime;

        assertThat(duration).isLessThan(100);
    }

    @Test
    void testCountQueryPerformance() {
        createTestProducts(10000);

        long startTime = System.currentTimeMillis();
        long count = productRepository.count();
        long duration = System.currentTimeMillis() - startTime;

        assertThat(duration).isLessThan(100);
        assertThat(count).isGreaterThan(0);
    }

    private void createTestProducts(int count) {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Product product = new Product();
            product.setProductName("Test Product " + i);
            product.setPrice(BigDecimal.valueOf(10.00 + (i % 1000)));
            product.setStockQuantity(100);
            products.add(product);

            if (i % 100 == 0) {
                productRepository.saveAll(products);
                products.clear();
                entityManager.flush();
                entityManager.clear();
            }
        }
        if (!products.isEmpty()) {
            productRepository.saveAll(products);
        }
    }
}

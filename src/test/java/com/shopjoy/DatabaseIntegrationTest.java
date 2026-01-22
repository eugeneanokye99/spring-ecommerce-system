package com.shopjoy;

import com.shopjoy.entity.Product;
import com.shopjoy.entity.User;
import com.shopjoy.entity.Order;
import com.shopjoy.repository.ProductRepository;
import com.shopjoy.repository.UserRepository;
import com.shopjoy.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.ConstraintViolationException;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class DatabaseIntegrationTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void productUniqueName_ShouldPreventDuplicates() {
        Product product1 = new Product();
        product1.setProductName("Unique Product");
        product1.setPrice(BigDecimal.valueOf(99.99));
        productRepository.save(product1);

        Product product2 = new Product();
        product2.setProductName("Unique Product");
        product2.setPrice(BigDecimal.valueOf(89.99));

        assertThatThrownBy(() -> {
            productRepository.save(product2);
            productRepository.flush();
        }).isInstanceOf(Exception.class);
    }

    @Test
    void productPrice_CannotBeNegative() {
        Product product = new Product();
        product.setProductName("Invalid Product");
        product.setPrice(BigDecimal.valueOf(-10.00));
        product.setStockQuantity(100);

        assertThatThrownBy(() -> {
            productRepository.save(product);
            productRepository.flush();
        }).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void userEmail_ShouldBeUnique() {
        User user1 = new User();
        user1.setUsername("user1");
        user1.setEmail("duplicate@example.com");
        user1.setPassword("password123");
        userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("user2");
        user2.setEmail("duplicate@example.com");
        user2.setPassword("password456");

        assertThatThrownBy(() -> {
            userRepository.save(user2);
            userRepository.flush();
        }).isInstanceOf(Exception.class);
    }

    @Test
    void cascadeDelete_ShouldDeleteRelatedEntities() {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password");
        userRepository.save(user);

        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(BigDecimal.valueOf(100.00));
        orderRepository.save(order);

        Long orderId = order.getOrderId();

        userRepository.delete(user);
        userRepository.flush();

        assertThat(orderRepository.findById(orderId)).isEmpty();
    }

    @Test
    void foreignKeyConstraint_ShouldPreventInvalidReferences() {
        Product product = new Product();
        product.setProductName("Test Product");
        product.setPrice(BigDecimal.valueOf(50.00));
        product.setStockQuantity(10);

        assertThatThrownBy(() -> {
            productRepository.save(product);
            productRepository.flush();
        }).isInstanceOf(Exception.class);
    }

    @Test
    void transactionIsolation_ShouldHandleConcurrentUpdates() {
        Product product = new Product();
        product.setProductName("Concurrent Product");
        product.setPrice(BigDecimal.valueOf(100.00));
        product.setStockQuantity(100);
        Product saved = productRepository.save(product);

        Product instance1 = productRepository.findById(saved.getProductId()).orElseThrow();
        Product instance2 = productRepository.findById(saved.getProductId()).orElseThrow();

        instance1.setStockQuantity(90);
        productRepository.save(instance1);

        instance2.setStockQuantity(80);
        productRepository.save(instance2);

        Product final Product = productRepository.findById(saved.getProductId()).orElseThrow();
        assertThat(finalProduct.getStockQuantity()).isEqualTo(80);
    }
}

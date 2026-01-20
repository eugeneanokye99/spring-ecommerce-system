package com.shopjoy.integration;

import com.shopjoy.dto.request.*;
import com.shopjoy.dto.response.*;
import com.shopjoy.entity.OrderStatus;
import com.shopjoy.exception.ResourceNotFoundException;
import com.shopjoy.service.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for Order management
 * Tests the full stack: Controller -> Service -> Repository -> Database
 */
@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
class OrderIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private InventoryService inventoryService;

    private Integer testUserId;

    @BeforeEach
    void setUp() {
        // Create test user
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("orderuser_" + System.currentTimeMillis());
        userRequest.setEmail("orderuser_" + System.currentTimeMillis() + "@example.com");
        userRequest.setPassword("SecurePass123!");
        userRequest.setFirstName("Order");
        userRequest.setLastName("Tester");
        UserResponse user = userService.registerUser(userRequest);
        testUserId = user.getUserId();

        // Create test category
        CreateCategoryRequest categoryRequest = new CreateCategoryRequest();
        categoryRequest.setCategoryName("Order Category " + System.currentTimeMillis());
        CategoryResponse category = categoryService.createCategory(categoryRequest);

        // Create test product
        CreateProductRequest productRequest = new CreateProductRequest();
        productRequest.setProductName("Order Product " + System.currentTimeMillis());
        productRequest.setDescription("Product for order testing");
        productRequest.setPrice(50.0);
        productRequest.setCostPrice(35.0);
        productRequest.setSku("ORDER-SKU-" + System.currentTimeMillis());
        productRequest.setBrand("Test Brand");
        productRequest.setCategoryId(category.getCategoryId());
        productRequest.setIsActive(true);
        ProductResponse product = productService.createProduct(productRequest);
        Integer testProductId = product.getProductId();

        // Create inventory for product
        inventoryService.createInventory(testProductId, 100, 10);

        // Create test address
        CreateAddressRequest addressRequest = new CreateAddressRequest();
        addressRequest.setUserId(testUserId);
        addressRequest.setStreetAddress("123 Test Street");
        addressRequest.setCity("Test City");
        addressRequest.setState("Test State");
        addressRequest.setPostalCode("12345");
        addressRequest.setCountry("Test Country");
        addressRequest.setIsDefault(true);
        addressService.createAddress(addressRequest);
    }

    @Test
    @Order(1)
    @DisplayName("Should create a new order successfully")
    void testCreateOrder() {
        // Arrange
        CreateOrderRequest orderRequest = new CreateOrderRequest();
        orderRequest.setUserId(testUserId);
        orderRequest.setShippingAddress("123 Test Street, Test City, Test State 12345");
        orderRequest.setNotes("Test order notes");

        // Act
        OrderResponse response = orderService.createOrder(orderRequest);

        // Assert
        assertNotNull(response);
        assertEquals(testUserId, response.getUserId());
        assertEquals(OrderStatus.PENDING, response.getStatus());
        assertNotNull(response.getCreatedAt());
    }

    @Test
    @Order(2)
    @DisplayName("Should retrieve order by ID")
    void testGetOrderById() {
        // Arrange
        CreateOrderRequest orderRequest = new CreateOrderRequest();
        orderRequest.setUserId(testUserId);
        orderRequest.setShippingAddress("123 Test Street, Test City, Test State 12345");

        OrderResponse createdOrder = orderService.createOrder(orderRequest);

        // Act
        OrderResponse retrievedOrder = orderService.getOrderById(createdOrder.getOrderId());

        // Assert
        assertNotNull(retrievedOrder);
        assertEquals(createdOrder.getOrderId(), retrievedOrder.getOrderId());
        assertEquals(createdOrder.getUserId(), retrievedOrder.getUserId());
    }

    @Test
    @Order(3)
    @DisplayName("Should throw exception when order not found")
    void testGetOrderByIdNotFound() {
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            orderService.getOrderById(999999);
        });
    }

    @Test
    @Order(4)
    @DisplayName("Should retrieve orders by user")
    void testGetOrdersByUser() {
        // Arrange
        CreateOrderRequest orderRequest = new CreateOrderRequest();
        orderRequest.setUserId(testUserId);
        orderRequest.setShippingAddress("123 Test Street, Test City, Test State 12345");

        orderService.createOrder(orderRequest);

        // Act
        List<OrderResponse> orders = orderService.getOrdersByUser(testUserId);

        // Assert
        assertNotNull(orders);
        assertFalse(orders.isEmpty());
        assertTrue(orders.stream().allMatch(o -> o.getUserId() == testUserId));
    }

    @Test
    @Order(5)
    @DisplayName("Should retrieve orders by status")
    void testGetOrdersByStatus() {
        // Arrange
        CreateOrderRequest orderRequest = new CreateOrderRequest();
        orderRequest.setUserId(testUserId);
        orderRequest.setShippingAddress("123 Test Street, Test City, Test State 12345");

        orderService.createOrder(orderRequest);

        // Act
        List<OrderResponse> pendingOrders = orderService.getOrdersByStatus(OrderStatus.PENDING);

        // Assert
        assertNotNull(pendingOrders);
        assertFalse(pendingOrders.isEmpty());
        assertTrue(pendingOrders.stream().allMatch(o -> o.getStatus() == OrderStatus.PENDING));
    }

    @Test
    @Order(6)
    @DisplayName("Should update order status successfully")
    void testUpdateOrderStatus() {
        // Arrange
        CreateOrderRequest orderRequest = new CreateOrderRequest();
        orderRequest.setUserId(testUserId);
        orderRequest.setShippingAddress("123 Test Street, Test City, Test State 12345");

        OrderResponse createdOrder = orderService.createOrder(orderRequest);

        // Act
        OrderResponse updatedOrder = orderService.updateOrderStatus(
            createdOrder.getOrderId(), 
            OrderStatus.PROCESSING
        );

        // Assert
        assertNotNull(updatedOrder);
        assertEquals(OrderStatus.PROCESSING, updatedOrder.getStatus());
    }

    @Test
    @Order(7)
    @DisplayName("Should confirm order successfully")
    void testConfirmOrder() {
        // Arrange
        CreateOrderRequest orderRequest = new CreateOrderRequest();
        orderRequest.setUserId(testUserId);
        orderRequest.setShippingAddress("123 Test Street, Test City, Test State 12345");

        OrderResponse createdOrder = orderService.createOrder(orderRequest);

        // Act
        OrderResponse confirmedOrder = orderService.confirmOrder(createdOrder.getOrderId());

        // Assert
        assertNotNull(confirmedOrder);
        assertEquals(OrderStatus.PROCESSING, confirmedOrder.getStatus());
    }

    @Test
    @Order(8)
    @DisplayName("Should retrieve pending orders")
    void testGetPendingOrders() {
        // Arrange
        CreateOrderRequest orderRequest = new CreateOrderRequest();
        orderRequest.setUserId(testUserId);
        orderRequest.setShippingAddress("123 Test Street, Test City, Test State 12345");

        orderService.createOrder(orderRequest);

        // Act
        List<OrderResponse> pendingOrders = orderService.getPendingOrders();

        // Assert
        assertNotNull(pendingOrders);
        assertFalse(pendingOrders.isEmpty());
        assertTrue(pendingOrders.stream().allMatch(o -> o.getStatus() == OrderStatus.PENDING));
    }
}

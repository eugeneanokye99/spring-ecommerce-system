package com.shopjoy.integration;

import com.shopjoy.dto.request.CreateUserRequest;
import com.shopjoy.dto.request.UpdateUserRequest;
import com.shopjoy.dto.response.UserResponse;
import com.shopjoy.exception.DuplicateResourceException;
import com.shopjoy.exception.ResourceNotFoundException;
import com.shopjoy.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for User management
 * Service-layer integration tests: Service -> Repository -> Database
 */
@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
class UserIntegrationTest {

    @Autowired
    private UserService userService;

    private CreateUserRequest testUserRequest;

    @BeforeEach
    void setUp() {
        testUserRequest = new CreateUserRequest();
        testUserRequest.setUsername("testuser_" + System.currentTimeMillis());
        testUserRequest.setEmail("testuser_" + System.currentTimeMillis() + "@example.com");
        testUserRequest.setPassword("SecurePass123!");
        testUserRequest.setFirstName("Test");
        testUserRequest.setLastName("User");
        testUserRequest.setPhone("+1234567890");
    }

    @Test
    @Order(1)
    @DisplayName("Should register a new user successfully")
    void testRegisterUser() {
        // Act
        UserResponse response = userService.registerUser(testUserRequest);

        // Assert
        assertNotNull(response);
        assertEquals(testUserRequest.getUsername(), response.getUsername());
        assertEquals(testUserRequest.getEmail(), response.getEmail());
        assertEquals(testUserRequest.getFirstName(), response.getFirstName());
        assertEquals(testUserRequest.getLastName(), response.getLastName());
        assertNotNull(response.getCreatedAt());
    }

    @Test
    @Order(2)
    @DisplayName("Should not allow duplicate username")
    void testDuplicateUsername() {
        // Arrange
        userService.registerUser(testUserRequest);

        CreateUserRequest duplicateRequest = new CreateUserRequest();
        duplicateRequest.setUsername(testUserRequest.getUsername());
        duplicateRequest.setEmail("different@example.com");
        duplicateRequest.setPassword("SecurePass123!");
        duplicateRequest.setFirstName("Different");
        duplicateRequest.setLastName("User");

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> {
            userService.registerUser(duplicateRequest);
        });
    }

    @Test
    @Order(3)
    @DisplayName("Should not allow duplicate email")
    void testDuplicateEmail() {
        // Arrange
        userService.registerUser(testUserRequest);

        CreateUserRequest duplicateRequest = new CreateUserRequest();
        duplicateRequest.setUsername("differentuser");
        duplicateRequest.setEmail(testUserRequest.getEmail());
        duplicateRequest.setPassword("SecurePass123!");
        duplicateRequest.setFirstName("Different");
        duplicateRequest.setLastName("User");

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> {
            userService.registerUser(duplicateRequest);
        });
    }

    @Test
    @Order(4)
    @DisplayName("Should retrieve user by ID")
    void testGetUserById() {
        // Arrange
        UserResponse createdUser = userService.registerUser(testUserRequest);

        // Act
        UserResponse retrievedUser = userService.getUserById(createdUser.getUserId());

        // Assert
        assertNotNull(retrievedUser);
        assertEquals(createdUser.getUserId(), retrievedUser.getUserId());
        assertEquals(createdUser.getUsername(), retrievedUser.getUsername());
        assertEquals(createdUser.getEmail(), retrievedUser.getEmail());
    }

    @Test
    @Order(5)
    @DisplayName("Should throw exception when user not found")
    void testGetUserByIdNotFound() {
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById(999999);
        });
    }

    @Test
    @Order(6)
    @DisplayName("Should retrieve user by username")
    void testGetUserByUsername() {
        // Arrange
        UserResponse createdUser = userService.registerUser(testUserRequest);

        // Act
        UserResponse retrievedUser = userService.getUserByUsername(createdUser.getUsername())
                .orElseThrow(() -> new AssertionError("User should exist"));

        // Assert
        assertNotNull(retrievedUser);
        assertEquals(createdUser.getUserId(), retrievedUser.getUserId());
        assertEquals(createdUser.getUsername(), retrievedUser.getUsername());
    }

    @Test
    @Order(7)
    @DisplayName("Should retrieve user by email")
    void testGetUserByEmail() {
        // Arrange
        UserResponse createdUser = userService.registerUser(testUserRequest);

        // Act
        UserResponse retrievedUser = userService.getUserByEmail(createdUser.getEmail())
                .orElseThrow(() -> new AssertionError("User should exist"));

        // Assert
        assertNotNull(retrievedUser);
        assertEquals(createdUser.getUserId(), retrievedUser.getUserId());
        assertEquals(createdUser.getEmail(), retrievedUser.getEmail());
    }

    @Test
    @Order(8)
    @DisplayName("Should update user profile successfully")
    void testUpdateUser() {
        // Arrange
        UserResponse createdUser = userService.registerUser(testUserRequest);

        UpdateUserRequest updateRequest = new UpdateUserRequest();
        updateRequest.setEmail("updated@example.com");
        updateRequest.setFirstName("UpdatedFirstName");
        updateRequest.setLastName("UpdatedLastName");
        updateRequest.setPhone("+9876543210");

        // Act
        UserResponse updatedUser = userService.updateUserProfile(createdUser.getUserId(), updateRequest);

        // Assert
        assertNotNull(updatedUser);
        assertEquals(createdUser.getUserId(), updatedUser.getUserId());
        assertEquals("updated@example.com", updatedUser.getEmail());
        assertEquals("UpdatedFirstName", updatedUser.getFirstName());
        assertEquals("UpdatedLastName", updatedUser.getLastName());
        assertEquals("+9876543210", updatedUser.getPhone());
        assertEquals(createdUser.getUsername(), updatedUser.getUsername()); // Username should not change
    }

    @Test
    @Order(9)
    @DisplayName("Should delete user successfully")
    void testDeleteUser() {
        // Arrange
        UserResponse createdUser = userService.registerUser(testUserRequest);

        // Act
        userService.deleteUser(createdUser.getUserId());

        // Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById(createdUser.getUserId());
        });
    }

    @Test
    @Order(10)
    @DisplayName("Should authenticate user with correct credentials")
    void testAuthenticateUser() {
        // Arrange
        UserResponse createdUser = userService.registerUser(testUserRequest);

        // Act
        UserResponse authenticatedUser = userService.authenticateUser(
            testUserRequest.getUsername(), 
            testUserRequest.getPassword()
        );

        // Assert
        assertNotNull(authenticatedUser);
        assertEquals(createdUser.getUserId(), authenticatedUser.getUserId());
        assertEquals(createdUser.getUsername(), authenticatedUser.getUsername());
    }

    @Test
    @Order(11)
    @DisplayName("Should fail authentication with wrong password")
    void testAuthenticateUserWrongPassword() {
        // Arrange
        userService.registerUser(testUserRequest);

        // Act & Assert
        assertThrows(com.shopjoy.exception.AuthenticationException.class, () -> {
            userService.authenticateUser(
                testUserRequest.getUsername(), 
                "WrongPassword123!"
            );
        });
    }
}

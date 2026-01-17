package com.shopjoy.controller;

import com.shopjoy.dto.request.CreateUserRequest;
import com.shopjoy.dto.request.UpdateUserRequest;
import com.shopjoy.dto.response.ApiResponse;
import com.shopjoy.dto.response.UserResponse;
import com.shopjoy.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for User management.
 * Base path: /api/v1/users
 * THIN CONTROLLER: Only handles HTTP concerns. All business logic and DTO↔Entity mapping done by services.
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Create a new user (register).
     * POST /api/v1/users/register
     * 
     * @param request the user creation request (username, email, password, userType)
     * @return created user with HTTP 201 status
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> registerUser(
            @Valid @RequestBody CreateUserRequest request) {
        UserResponse response = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "User registered successfully"));
    }

    /**
     * Get user by ID.
     * GET /api/v1/users/{id}
     * 
     * @param id the user ID
     * @return user details with HTTP 200 status
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Integer id) {
        UserResponse response = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(response, "User retrieved successfully"));
    }

    /**
     * Get all users.
     * GET /api/v1/users
     * 
     * @return list of all users with HTTP 200 status
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> response = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(response, "Users retrieved successfully"));
    }

    /**
     * Update user profile.
     * PUT /api/v1/users/{id}
     * 
     * @param id the user ID
     * @param request the update request (username, email, firstName, lastName, phone, profileImageUrl)
     * @return updated user details with HTTP 200 status
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserProfile(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateUserRequest request) {
        UserResponse response = userService.updateUserProfile(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "User profile updated successfully"));
    }

    /**
     * Delete user.
     * DELETE /api/v1/users/{id}
     * 
     * @param id the user ID
     * @return success message with HTTP 200 status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success(null, "User deleted successfully"));
    }

    /**
     * Find user by email.
     * GET /api/v1/users/email/{email}
     * 
     * @param email the email address
     * @return user details with HTTP 200 status
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserByEmail(@PathVariable String email) {
        UserResponse response = userService.getUserByEmail(email)
                .orElseThrow(() -> new com.shopjoy.exception.ResourceNotFoundException(
                        "User", "email", email));
        return ResponseEntity.ok(ApiResponse.success(response, "User found by email"));
    }

    /**
     * Authenticate user (login).
     * POST /api/v1/users/authenticate
     * 
     * @param request the authentication request (username, password)
     * @return authenticated user details with HTTP 200 status
     */
    @PostMapping("/authenticate")
    public ResponseEntity<ApiResponse<UserResponse>> authenticateUser(
            @Valid @RequestBody java.util.Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        UserResponse response = userService.authenticateUser(username, password);
        return ResponseEntity.ok(ApiResponse.success(response, "User authenticated successfully"));
    }

    /**
     * Change user password.
     * PUT /api/v1/users/{id}/password
     * 
     * @param id the user ID
     * @param request the password change request (oldPassword, newPassword)
     * @return success message with HTTP 200 status
     */
    @PutMapping("/{id}/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @PathVariable Integer id,
            @Valid @RequestBody java.util.Map<String, String> request) {
        String oldPassword = request.get("oldPassword");
        String newPassword = request.get("newPassword");
        userService.changePassword(id, oldPassword, newPassword);
        return ResponseEntity.ok(ApiResponse.success(null, "Password changed successfully"));
    }
}

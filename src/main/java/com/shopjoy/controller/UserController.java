package com.shopjoy.controller;

import com.shopjoy.dto.mapper.UserMapper;
import com.shopjoy.dto.request.CreateUserRequest;
import com.shopjoy.dto.request.UpdateUserRequest;
import com.shopjoy.dto.response.ApiResponse;
import com.shopjoy.dto.response.UserResponse;
import com.shopjoy.entity.User;
import com.shopjoy.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for User management.
 * Base path: /api/v1/users
 * REFACTORED: Only calls methods that exist in UserService
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
     * POST /api/v1/users
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody CreateUserRequest request) {
        
        User user = UserMapper.toUser(request);
        User createdUser = userService.registerUser(user);  // ← Service method exists
        UserResponse response = UserMapper.toUserResponse(createdUser);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "User created successfully"));
    }
    
    /**
     * Get user by ID.
     * GET /api/v1/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable int id) {
        
        User user = userService.getUserById(id);  // ← Service method exists
        UserResponse response = UserMapper.toUserResponse(user);
        
        return ResponseEntity.ok(ApiResponse.success(response, "User retrieved successfully"));
    }
    
    /**
     * Get all users.
     * GET /api/v1/users
     */
    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        
        List<User> users = userService.getAllUsers();  // ← Service method exists
        List<UserResponse> userResponses = UserMapper.toUserResponseList(users);
        
        return ResponseEntity.ok(ApiResponse.success(userResponses, "Users retrieved successfully"));
    }
    
    /**
     * Update user.
     * PUT /api/v1/users/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable int id,
            @Valid @RequestBody UpdateUserRequest request) {
        
        User existingUser = userService.getUserById(id);  // ← Service method exists
        UserMapper.updateUserFromRequest(existingUser, request);
        User updatedUser = userService.updateUserProfile(existingUser);  // ← Service method exists
        UserResponse response = UserMapper.toUserResponse(updatedUser);
        
        return ResponseEntity.ok(ApiResponse.success(response, "User updated successfully"));
    }
    
    /**
     * Delete user.
     * DELETE /api/v1/users/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable int id) {
        
        userService.deleteUser(id);  // ← Service method exists
        
        return ResponseEntity.ok(ApiResponse.success(null, "User deleted successfully"));
    }
    
    // ==================== CUSTOM ENDPOINTS ====================
    
    /**
     * Find user by email.
     * GET /api/v1/users/email/{email}
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserByEmail(@PathVariable String email) {
        
        User user = userService.getUserByEmail(email)  // ← Service method exists (returns Optional)
                .orElseThrow(() -> new com.shopjoy.exception.ResourceNotFoundException(
                        "User not found with email: " + email));
        UserResponse response = UserMapper.toUserResponse(user);
        
        return ResponseEntity.ok(ApiResponse.success(response, "User found by email"));
    }
    
    /**
     * Change user password.
     * PUT /api/v1/users/{id}/password
     * Request body: { "oldPassword": "current", "newPassword": "newSecret123" }
     */
    @PutMapping("/{id}/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @PathVariable int id,
            @RequestBody java.util.Map<String, String> passwordRequest) {
        
        String oldPassword = passwordRequest.get("oldPassword");
        String newPassword = passwordRequest.get("newPassword");
        
        if (oldPassword == null || oldPassword.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Old password is required"));
        }
        
        if (newPassword == null || newPassword.length() < 8) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("New password must be at least 8 characters"));
        }
        
        userService.changePassword(id, oldPassword, newPassword);  // ← Service method exists
        
        return ResponseEntity.ok(ApiResponse.success(null, "Password changed successfully"));
    }
}

package com.shopjoy.dto.mapper;

import com.shopjoy.dto.request.CreateUserRequest;
import com.shopjoy.dto.request.UpdateUserRequest;
import com.shopjoy.dto.response.UserResponse;
import com.shopjoy.entity.User;
import com.shopjoy.entity.UserType;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper utility for converting between User entity and DTOs.
 * All methods are static - no need to create an instance.
 * 
 * Usage examples:
 * - User user = UserMapper.toUser(createRequest);
 * - UserResponse response = UserMapper.toUserResponse(user);
 * - UserMapper.updateUserFromRequest(existingUser, updateRequest);
 */
public class UserMapper {
    
    /**
     * Converts CreateUserRequest to User entity.
     * Use this when creating a new user from API request.
     * 
     * @param request the create request from API
     * @return new User entity (not saved yet)
     */
    public static User toUser(CreateUserRequest request) {
        if (request == null) {
            return null;
        }
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(request.getPassword()); // Note: Password will be hashed by service
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setUserType(UserType.CUSTOMER); // Default to CUSTOMER
        
        return user;
    }
    
    /**
     * Converts User entity to UserResponse.
     * Use this when sending user data back to API clients.
     * IMPORTANT: Password is never included in the response!
     * 
     * @param user the user entity from database
     * @return response DTO safe to send to clients
     */
    public static UserResponse toUserResponse(User user) {
        if (user == null) {
            return null;
        }
        
        return new UserResponse(
            user.getUserId(),
            user.getUsername(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName(),
            user.getPhone(),
            user.getUserType(),
            user.getCreatedAt()
        );
    }
    
    /**
     * Converts a list of User entities to a list of UserResponse DTOs.
     * Use this for endpoints that return multiple users.
     * 
     * @param users list of user entities
     * @return list of response DTOs
     */
    public static List<UserResponse> toUserResponseList(List<User> users) {
        if (users == null) {
            return null;
        }
        
        List<UserResponse> responses = new ArrayList<>();
        for (User user : users) {
            responses.add(toUserResponse(user));
        }
        return responses;
    }
    
    /**
     * Updates an existing User entity with data from UpdateUserRequest.
     * Only updates fields that are not null in the request.
     * This allows partial updates (e.g., only changing email).
     * 
     * @param user the existing user entity to update
     * @param request the update request with new values
     */
    public static void updateUserFromRequest(User user, UpdateUserRequest request) {
        if (user == null || request == null) {
            return;
        }
        
        // Only update fields that are provided (not null)
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
    }
}

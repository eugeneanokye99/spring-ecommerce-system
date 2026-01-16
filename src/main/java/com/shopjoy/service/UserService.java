package com.shopjoy.service;

import com.shopjoy.entity.User;
import com.shopjoy.entity.UserType;
import com.shopjoy.exception.*;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for User-related business operations.
 * Handles user registration, authentication, profile management, and user queries.
 */
public interface UserService {
    
    /**
     * Registers a new user in the system.
     * Validates that username and email are unique before creating the account.
     * Hashes the password before storing it.
     * 
     * @param user the user to register (password should be plain text)
     * @return the created user with generated ID and hashed password
     * @throws DuplicateResourceException if username or email already exists
     * @throws ValidationException if user data is invalid
     */
    User registerUser(User user);
    
    /**
     * Authenticates a user with username and plain text password.
     * 
     * @param username the username
     * @param password the plain text password
     * @return the authenticated user
     * @throws AuthenticationException if credentials are invalid
     */
    User authenticateUser(String username, String password);
    
    /**
     * Retrieves a user by their ID.
     * 
     * @param userId the user ID
     * @return the user
     * @throws ResourceNotFoundException if user not found
     */
    User getUserById(Integer userId);
    
    /**
     * Retrieves a user by their email address.
     * 
     * @param email the email address
     * @return Optional containing the user if found
     */
    Optional<User> getUserByEmail(String email);
    
    /**
     * Retrieves a user by their username.
     * 
     * @param username the username
     * @return Optional containing the user if found
     */
    Optional<User> getUserByUsername(String username);
    
    /**
     * Retrieves all users in the system.
     * 
     * @return list of all users
     */
    List<User> getAllUsers();
    
    /**
     * Retrieves all users of a specific type.
     * 
     * @param userType the user type (CUSTOMER or ADMIN)
     * @return list of users with the specified type
     */
    List<User> getUsersByType(UserType userType);
    
    /**
     * Updates an existing user's profile information.
     * Username and email uniqueness are validated if changed.
     * Password is not updated through this method.
     * 
     * @param user the user with updated information
     * @return the updated user
     * @throws ResourceNotFoundException if user not found
     * @throws DuplicateResourceException if new username/email already exists
     * @throws ValidationException if user data is invalid
     */
    User updateUserProfile(User user);
    
    /**
     * Changes a user's password.
     * 
     * @param userId the user ID
     * @param oldPassword the current password (for verification)
     * @param newPassword the new password (plain text)
     * @throws ResourceNotFoundException if user not found
     * @throws AuthenticationException if old password is incorrect
     * @throws ValidationException if new password doesn't meet requirements
     */
    void changePassword(Integer userId, String oldPassword, String newPassword);
    
    /**
     * Deletes a user from the system.
     * Should check for related data (orders, reviews, etc.) before deletion.
     * 
     * @param userId the user ID
     * @throws ResourceNotFoundException if user not found
     * @throws BusinessException if user has related data that prevents deletion
     */
    void deleteUser(Integer userId);
    
    /**
     * Checks if an email address is already registered.
     * 
     * @param email the email to check
     * @return true if email exists, false otherwise
     */
    boolean isEmailTaken(String email);
    
    /**
     * Checks if a username is already taken.
     * 
     * @param username the username to check
     * @return true if username exists, false otherwise
     */
    boolean isUsernameTaken(String username);
}

package com.shopjoy.service.impl;

import com.shopjoy.entity.User;
import com.shopjoy.entity.UserType;
import com.shopjoy.exception.AuthenticationException;
import com.shopjoy.exception.DuplicateResourceException;
import com.shopjoy.exception.ResourceNotFoundException;
import com.shopjoy.exception.ValidationException;
import com.shopjoy.repository.UserRepository;
import com.shopjoy.service.UserService;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    
    private final UserRepository userRepository;
    
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    @Transactional(readOnly = false)
    public User registerUser(User user) {
        logger.info("Attempting to register new user with username: {}", user.getUsername());
        
        validateUserData(user);
        
        if (userRepository.usernameExists(user.getUsername())) {
            logger.warn("Registration failed: Username already exists: {}", user.getUsername());
            throw new DuplicateResourceException("User", "username", user.getUsername());
        }
        
        if (userRepository.emailExists(user.getEmail())) {
            logger.warn("Registration failed: Email already exists: {}", user.getEmail());
            throw new DuplicateResourceException("User", "email", user.getEmail());
        }
        
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        User createdUser = userRepository.save(user);
        logger.info("Successfully registered user with ID: {}", createdUser.getUserId());
        
        return createdUser;
    }
    
    @Override
    public User authenticateUser(String username, String password) {
        logger.info("Authentication attempt for username: {}", username);
        
        if (username == null || username.trim().isEmpty()) {
            throw new ValidationException("Username cannot be empty");
        }
        
        if (password == null || password.isEmpty()) {
            throw new ValidationException("Password cannot be empty");
        }
        
        Optional<User> userOpt = userRepository.authenticate(username, password);
        
        if (userOpt.isEmpty()) {
            logger.warn("Authentication failed for username: {}", username);
            throw new AuthenticationException();
        }
        
        logger.info("Successfully authenticated user: {}", username);
        return userOpt.get();
    }
    
    @Override
    public User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }
    
    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    @Override
    public List<User> getUsersByType(UserType userType) {
        if (userType == null) {
            throw new ValidationException("User type cannot be null");
        }
        return userRepository.findByUserType(userType);
    }
    
    @Override
    @Transactional(readOnly = false)
    public User updateUserProfile(User user) {
        logger.info("Updating profile for user ID: {}", user.getUserId());
        
        User existingUser = getUserById(user.getUserId());
        
        validateUserData(user);
        
        if (!existingUser.getUsername().equals(user.getUsername())) {
            if (userRepository.usernameExists(user.getUsername())) {
                throw new DuplicateResourceException("User", "username", user.getUsername());
            }
        }
        
        if (!existingUser.getEmail().equals(user.getEmail())) {
            if (userRepository.emailExists(user.getEmail())) {
                throw new DuplicateResourceException("User", "email", user.getEmail());
            }
        }
        
        user.setPasswordHash(existingUser.getPasswordHash());
        user.setCreatedAt(existingUser.getCreatedAt());
        user.setUpdatedAt(LocalDateTime.now());
        
        User updatedUser = userRepository.update(user);
        logger.info("Successfully updated user profile for ID: {}", user.getUserId());
        
        return updatedUser;
    }
    
    @Override
    @Transactional(readOnly = false)
    public void changePassword(Integer userId, String oldPassword, String newPassword) {
        logger.info("Password change requested for user ID: {}", userId);
        
        User user = getUserById(userId);
        
        if (!BCrypt.checkpw(oldPassword, user.getPasswordHash())) {
            logger.warn("Password change failed: Incorrect old password for user ID: {}", userId);
            throw new AuthenticationException("Current password is incorrect");
        }
        
        validatePassword(newPassword);
        
        userRepository.changePassword(userId, newPassword);
        logger.info("Successfully changed password for user ID: {}", userId);
    }
    
    @Override
    @Transactional(readOnly = false)
    public void deleteUser(Integer userId) {
        logger.info("Deleting user with ID: {}", userId);
        
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }
        
        userRepository.delete(userId);
        logger.info("Successfully deleted user with ID: {}", userId);
    }
    
    @Override
    public boolean isEmailTaken(String email) {
        return userRepository.emailExists(email);
    }
    
    @Override
    public boolean isUsernameTaken(String username) {
        return userRepository.usernameExists(username);
    }
    
    private void validateUserData(User user) {
        if (user == null) {
            throw new ValidationException("User data cannot be null");
        }
        
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new ValidationException("username", "must not be empty");
        }
        
        if (user.getUsername().length() < 3 || user.getUsername().length() > 50) {
            throw new ValidationException("username", "must be between 3 and 50 characters");
        }
        
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new ValidationException("email", "must not be empty");
        }
        
        if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ValidationException("email", "must be a valid email address");
        }
        
        if (user.getUserType() == null) {
            throw new ValidationException("userType", "must not be null");
        }
    }
    
    private void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new ValidationException("password", "must not be empty");
        }
        
        if (password.length() < 8) {
            throw new ValidationException("password", "must be at least 8 characters long");
        }
        
        if (!password.matches(".*[A-Z].*")) {
            throw new ValidationException("password", "must contain at least one uppercase letter");
        }
        
        if (!password.matches(".*[a-z].*")) {
            throw new ValidationException("password", "must contain at least one lowercase letter");
        }
        
        if (!password.matches(".*\\d.*")) {
            throw new ValidationException("password", "must contain at least one digit");
        }
    }
}

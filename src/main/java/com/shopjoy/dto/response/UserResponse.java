package com.shopjoy.dto.response;

import com.shopjoy.entity.UserType;
import java.time.LocalDateTime;

/**
 * Response DTO for User.
 * This is what the API sends back to clients.
 * IMPORTANT: Password is NEVER included in responses!
 */
public class UserResponse {
    
    private int userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private UserType userType;
    private LocalDateTime createdAt;
    
    // No-argument constructor
    public UserResponse() {
    }
    
    // Constructor with all fields
    public UserResponse(int userId, String username, String email, String firstName, 
                       String lastName, String phone, UserType userType, LocalDateTime createdAt) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.userType = userType;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public UserType getUserType() {
        return userType;
    }
    
    public void setUserType(UserType userType) {
        this.userType = userType;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

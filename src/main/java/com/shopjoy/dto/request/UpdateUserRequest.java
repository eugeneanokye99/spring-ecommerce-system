package com.shopjoy.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for updating an existing user.
 * All fields are optional - only send fields you want to update.
 * NOTE: Username and password are NOT included here - use separate endpoints for those.
 */
public class UpdateUserRequest {
    
    // Email: optional, but must be valid if provided
    @Email(message = "Email must be valid")
    private String email;
    
    // First name: optional
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    private String firstName;
    
    // Last name: optional
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    private String lastName;
    
    // Phone: optional
    private String phone;
    
    // No-argument constructor
    public UpdateUserRequest() {
    }
    
    // Getters and Setters
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
}

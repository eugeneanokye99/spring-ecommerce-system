package com.shopjoy.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Request DTO for updating an existing user.
 * All fields are optional - only send fields you want to update.
 * NOTE: Username and password are NOT included here - use separate endpoints for those.
 */
@Setter
@Getter
public class UpdateUserRequest {


    @Email(message = "Email must be valid")
    private String email;
    
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    private String firstName;
    
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    private String lastName;
    
    private String phone;

    /**
     * Instantiates a new Update user request.
     */
    public UpdateUserRequest() {
    }

}

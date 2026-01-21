package com.shopjoy.graphql.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserInput(
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50)
        String username,
        
        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        String email,
        
        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters")
        String password,
        
        String firstName,
        String lastName,
        String phone,
        
        @NotBlank(message = "User type is required")
        String userType
) {}

package com.shopjoy.graphql.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserInput(
        @Size(min = 3, max = 50)
        String username,
        
        @Email(message = "Email should be valid")
        String email,
        
        String firstName,
        String lastName,
        String phone
) {}

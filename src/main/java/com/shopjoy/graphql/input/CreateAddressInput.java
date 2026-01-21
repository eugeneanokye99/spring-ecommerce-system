package com.shopjoy.graphql.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateAddressInput(
        @NotNull(message = "User ID is required")
        Long userId,
        
        @NotBlank(message = "Address type is required")
        String addressType,
        
        @NotBlank(message = "Street is required")
        String street,
        
        @NotBlank(message = "City is required")
        String city,
        
        @NotBlank(message = "State is required")
        String state,
        
        @NotBlank(message = "Postal code is required")
        String postalCode,
        
        @NotBlank(message = "Country is required")
        String country,
        
        Boolean isDefault
) {}

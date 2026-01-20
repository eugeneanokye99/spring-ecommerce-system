package com.shopjoy.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "Request for user registration with all required account details")
@Setter
@Getter
public class CreateUserRequest {

    @Schema(description = "Unique username for login", example = "johndoe123", required = true)
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;
    
    @Schema(description = "User email address", example = "john.doe@example.com", required = true)
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;
    
    @Schema(description = "User password (minimum 8 characters)", example = "SecureP@ssw0rd", required = true)
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
    
    @Schema(description = "User first name", example = "John", required = true)
    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    private String firstName;
    
    @Schema(description = "User last name", example = "Doe", required = true)
    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    private String lastName;
    
    @Schema(description = "User phone number", example = "+1234567890")
    private String phone;

    public CreateUserRequest() {
    }

}

package com.shopjoy.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "Request for updating user profile - all fields are optional")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {

    @Schema(description = "Updated email address", example = "john.newemail@example.com")
    @Email(message = "Email must be valid")
    private String email;

    @Schema(description = "Updated first name", example = "John")
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    private String firstName;

    @Schema(description = "Updated last name", example = "Doe")
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    private String lastName;

    @Schema(description = "Updated phone number", example = "+1987654321")
    private String phone;

}

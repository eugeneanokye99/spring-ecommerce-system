package com.shopjoy.dto.response;

import com.shopjoy.entity.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Response DTO for User.
 * This is what the API sends back to clients.
 * IMPORTANT: Password is NEVER included in responses!
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private int userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private UserType userType;
    private LocalDateTime createdAt;


}

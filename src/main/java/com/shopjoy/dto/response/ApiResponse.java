package com.shopjoy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Generic wrapper for all API responses.
 * This ensures all API responses have a consistent structure.
 * <p>
 * Example usage:
 * - Success: ApiResponse.success(userResponse, "User created successfully")
 * - Error: ApiResponse.error("User not found")
 * - Validation Error: ApiResponse.validationError("Validation failed", errorList)
 *
 * @param <T> the type parameter
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private List<ErrorDetail> errors;
    private LocalDateTime timestamp;

    /**
     * Creates a success response with data and message
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage(message);
        response.setData(data);
        response.setTimestamp(LocalDateTime.now());
        return response;
    }

    /**
     * Creates a simple error response with just a message
     */
    public static <T> ApiResponse<T> error(String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setMessage(message);
        response.setTimestamp(LocalDateTime.now());
        return response;
    }
    
    /**
     * Creates an error response with detailed error information
     */
    public static <T> ApiResponse<T> error(String message, List<ErrorDetail> errors) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setMessage(message);
        response.setErrors(errors);
        response.setTimestamp(LocalDateTime.now());
        return response;
    }
    
    /**
     * Creates an error response with a single error detail
     */
    public static <T> ApiResponse<T> error(String message, ErrorDetail error) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setMessage(message);
        response.setErrors(List.of(error));
        response.setTimestamp(LocalDateTime.now());
        return response;
    }
    
    /**
     * Creates a validation error response
     */
    public static <T> ApiResponse<T> validationError(String message, List<ErrorDetail> errors) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setMessage(message);
        response.setErrors(errors);
        response.setTimestamp(LocalDateTime.now());
        return response;
    }
    
    /**
     * Creates a not found error response
     */
    public static <T> ApiResponse<T> notFound(String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setMessage(message);
        response.setTimestamp(LocalDateTime.now());
        return response;
    }
    
    /**
     * Creates a conflict error response
     */
    public static <T> ApiResponse<T> conflict(String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setMessage(message);
        response.setTimestamp(LocalDateTime.now());
        return response;
    }
    
    /**
     * Creates a conflict error response with error details
     */
    public static <T> ApiResponse<T> conflict(String message, ErrorDetail error) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setMessage(message);
        response.setErrors(List.of(error));
        response.setTimestamp(LocalDateTime.now());
        return response;
    }
}

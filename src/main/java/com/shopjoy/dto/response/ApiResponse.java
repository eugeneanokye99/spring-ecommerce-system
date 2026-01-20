package com.shopjoy.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Generic wrapper for all API responses ensuring consistent structure")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    @Schema(description = "Indicates if the request was successful", example = "true")
    private boolean success;
    
    @Schema(description = "Human-readable response message", example = "Product created successfully")
    private String message;
    
    @Schema(description = "Response payload data")
    private T data;
    
    @Schema(description = "List of error details if request failed")
    private List<ErrorDetail> errors;
    
    @Schema(description = "Timestamp when the response was generated", example = "2024-01-20T10:30:00")
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

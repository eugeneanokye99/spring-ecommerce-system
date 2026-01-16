package com.shopjoy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Generic wrapper for all API responses.
 * This ensures all API responses have a consistent structure.
 * <p>
 * Example usage:
 * - Success: ApiResponse.success(userResponse, "User created successfully")
 * - Error: ApiResponse.error("User not found")
 *
 * @param <T> the type parameter
 */
@Setter
@Getter
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;


    /**
     * Success api response.
     *
     * @param <T>     the type parameter
     * @param data    the data
     * @param message the message
     * @return the api response
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, message, data);
    }

    /**
     * Error api response.
     *
     * @param <T>     the type parameter
     * @param message the message
     * @return the api response
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }

}

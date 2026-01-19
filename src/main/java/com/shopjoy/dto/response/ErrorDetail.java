package com.shopjoy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a single validation or business error detail.
 * Used to provide detailed information about what went wrong.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetail {
    
    /**
     * The field that caused the error (e.g., "email", "price")
     */
    private String field;
    
    /**
     * Human-readable error message
     */
    private String message;
    
    /**
     * The value that was rejected (can be null)
     */
    private Object rejectedValue;
    
    /**
     * Machine-readable error code (e.g., "REQUIRED", "INVALID_FORMAT")
     */
    private String errorCode;
    
    /**
     * Convenience constructor for simple field errors without rejected value
     */
    public ErrorDetail(String field, String message, String errorCode) {
        this.field = field;
        this.message = message;
        this.errorCode = errorCode;
    }
    
    /**
     * Convenience constructor for general errors without field context
     */
    public ErrorDetail(String message, String errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }
}

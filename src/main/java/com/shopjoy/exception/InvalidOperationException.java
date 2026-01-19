package com.shopjoy.exception;

import lombok.Getter;

/**
 * Exception thrown when an operation cannot be performed due to invalid state or business rules.
 * Example: Trying to cancel an already shipped order, or activating an already active product.
 */
@Getter
public class InvalidOperationException extends BusinessException {
    
    private final String operation;
    private final String reason;
    
    public InvalidOperationException(String operation, String reason) {
        super(String.format("Cannot perform operation '%s': %s", operation, reason), "INVALID_OPERATION");
        this.operation = operation;
        this.reason = reason;
    }
    
    public InvalidOperationException(String message) {
        super(message, "INVALID_OPERATION");
        this.operation = null;
        this.reason = null;
    }
}

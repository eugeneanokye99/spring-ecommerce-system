package com.shopjoy.exception;

public class ValidationException extends BusinessException {
    
    public ValidationException(String message) {
        super(message, "VALIDATION_ERROR");
    }
    
    public ValidationException(String fieldName, String validationRule) {
        super(String.format("Validation failed for field '%s': %s", fieldName, validationRule), "VALIDATION_ERROR");
    }
}

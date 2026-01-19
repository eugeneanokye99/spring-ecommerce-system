package com.shopjoy.exception;

import com.shopjoy.dto.response.ApiResponse;
import com.shopjoy.dto.response.ErrorDetail;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Global exception handler that catches all exceptions thrown in the application
 * and converts them to consistent API responses with appropriate HTTP status codes.
 * 
 * This class ensures:
 * - Consistent error response format across the entire API
 * - Appropriate HTTP status codes for different error types
 * - Detailed validation error information
 * - Proper logging for debugging
 * - Security by not exposing internal details in production
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    /**
     * Handles validation errors from @Valid annotation on request DTOs.
     * Returns 400 Bad Request with detailed field-level error information.
     * Example: When CreateProductRequest has @NotBlank on productName but client sends null
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        logger.warn("Validation failed: {}", ex.getMessage());
        
        List<ErrorDetail> errors = new ArrayList<>();
        
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            ErrorDetail error = new ErrorDetail(
                    fieldError.getField(),
                    fieldError.getDefaultMessage(),
                    fieldError.getRejectedValue(),
                    fieldError.getCode() != null ? fieldError.getCode().toUpperCase() : "VALIDATION_ERROR"
            );
            errors.add(error);
        }
        
        ApiResponse<Object> response = ApiResponse.validationError(
                "Validation failed for one or more fields",
                errors
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    /**
     * Handles validation errors on @PathVariable and @RequestParam annotations.
     * Returns 400 Bad Request.
     * Example: When @Positive is on @PathVariable Integer id but client sends -1
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleConstraintViolation(ConstraintViolationException ex) {
        logger.warn("Constraint violation: {}", ex.getMessage());
        
        List<ErrorDetail> errors = ex.getConstraintViolations().stream()
                .map(violation -> {
                    String propertyPath = getPropertyName(violation.getPropertyPath().toString());
                    return new ErrorDetail(
                            propertyPath,
                            violation.getMessage(),
                            violation.getInvalidValue(),
                            "CONSTRAINT_VIOLATION"
                    );
                })
                .collect(Collectors.toList());
        
        ApiResponse<Object> response = ApiResponse.validationError(
                "Request parameter validation failed",
                errors
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    /**
     * Handles ResourceNotFoundException - when entity not found by ID.
     * Returns 404 Not Found.
     * Example: GET /api/v1/products/999 when product 999 doesn't exist
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        logger.info("Resource not found: {}", ex.getMessage());
        
        ErrorDetail error = new ErrorDetail(
                ex.getFieldName() != null ? ex.getFieldName() : "resource",
                ex.getMessage(),
                ex.getFieldValue(),
                ex.getErrorCode()
        );
        
        ApiResponse<Object> response = ApiResponse.error(ex.getMessage(), error);
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    
    /**
     * Handles DuplicateResourceException - when trying to create a resource that already exists.
     * Returns 409 Conflict.
     * Example: Creating user with email that already exists
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<Object>> handleDuplicateResource(DuplicateResourceException ex) {
        logger.warn("Duplicate resource: {}", ex.getMessage());
        
        ErrorDetail error = new ErrorDetail(
                ex.getMessage(),
                ex.getErrorCode()
        );
        
        ApiResponse<Object> response = ApiResponse.conflict(ex.getMessage(), error);
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
    
    /**
     * Handles ValidationException - business rule validation failures.
     * Returns 400 Bad Request.
     * Example: Price cannot be negative, stock quantity invalid, etc.
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<Object>> handleBusinessValidation(ValidationException ex) {
        logger.warn("Business validation failed: {}", ex.getMessage());
        
        ErrorDetail error = new ErrorDetail(
                ex.getMessage(),
                ex.getErrorCode()
        );
        
        ApiResponse<Object> response = ApiResponse.error(ex.getMessage(), error);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    /**
     * Handles InsufficientStockException - when order quantity exceeds available stock.
     * Returns 400 Bad Request.
     * Example: Trying to order 100 units when only 50 are in stock
     */
    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ApiResponse<Object>> handleInsufficientStock(InsufficientStockException ex) {
        logger.warn("Insufficient stock: {}", ex.getMessage());
        
        ErrorDetail error = new ErrorDetail(
                "quantity",
                ex.getMessage(),
                ex.getErrorCode()
        );
        
        ApiResponse<Object> response = ApiResponse.error(ex.getMessage(), error);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    /**
     * Handles InvalidOperationException - when operation cannot be performed due to business rules.
     * Returns 400 Bad Request.
     * Example: Canceling an already shipped order
     */
    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidOperation(InvalidOperationException ex) {
        logger.warn("Invalid operation: {}", ex.getMessage());
        
        ErrorDetail error = new ErrorDetail(
                ex.getOperation() != null ? ex.getOperation() : "operation",
                ex.getMessage(),
                ex.getErrorCode()
        );
        
        ApiResponse<Object> response = ApiResponse.error(ex.getMessage(), error);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    /**
     * Handles InvalidOrderStateException - order state transition errors.
     * Returns 400 Bad Request.
     */
    @ExceptionHandler(InvalidOrderStateException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidOrderState(InvalidOrderStateException ex) {
        logger.warn("Invalid order state: {}", ex.getMessage());
        
        ErrorDetail error = new ErrorDetail(
                ex.getMessage(),
                ex.getErrorCode()
        );
        
        ApiResponse<Object> response = ApiResponse.error(ex.getMessage(), error);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    /**
     * Handles malformed JSON requests.
     * Returns 400 Bad Request.
     * 
     * Example: Invalid JSON syntax, missing quotes, wrong data type
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Object>> handleMalformedJson(HttpMessageNotReadableException ex) {
        logger.warn("Malformed JSON request: {}", ex.getMessage());
        
        String message = "Malformed JSON request. Please check your request body format.";
        if (ex.getCause() != null) {
            String causeMessage = ex.getCause().getMessage();
            if (causeMessage != null && causeMessage.contains("Cannot deserialize")) {
                message = "Invalid data type in request. Please check field types.";
            }
        }
        
        ErrorDetail error = new ErrorDetail(
                "request",
                message,
                "MALFORMED_JSON"
        );
        
        ApiResponse<Object> response = ApiResponse.error(message, error);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    /**
     * Handles type mismatch errors for path variables and request parameters.
     * Returns 400 Bad Request.
     * Example: Expecting Integer but got "abc"
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        logger.warn("Type mismatch: {}", ex.getMessage());
        
        String message = String.format("Invalid value '%s' for parameter '%s'. Expected type: %s",
                ex.getValue(),
                ex.getName(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown");
        
        ErrorDetail error = new ErrorDetail(
                ex.getName(),
                message,
                ex.getValue(),
                "TYPE_MISMATCH"
        );
        
        ApiResponse<Object> response = ApiResponse.error(message, error);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    /**
     * Handles database constraint violations (unique constraints, foreign key violations, etc.).
     * Returns 409 Conflict.
     * Example: Violating unique email constraint
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        logger.error("Data integrity violation: {}", ex.getMessage());
        
        String message = "Data integrity violation. This operation conflicts with existing data.";
        
        // Try to provide more specific message based on constraint violation
        if (ex.getMessage() != null) {
            if (ex.getMessage().contains("unique") || ex.getMessage().contains("duplicate")) {
                message = "A record with this value already exists. Please use a unique value.";
            } else if (ex.getMessage().contains("foreign key")) {
                message = "Referenced resource does not exist. Please check your input.";
            } else if (ex.getMessage().contains("not-null")) {
                message = "Required field is missing. Please provide all required fields.";
            }
        }
        
        ErrorDetail error = new ErrorDetail(
                message,
                "DATA_INTEGRITY_VIOLATION"
        );
        
        ApiResponse<Object> response = ApiResponse.conflict(message, error);
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
    
    /**
     * Handles general database/JDBC errors.
     * Returns 500 Internal Server Error.
     * IMPORTANT: Does not expose internal database details to clients for security.
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResponse<Object>> handleDatabaseError(DataAccessException ex) {
        logger.error("Database error occurred", ex);
        
        String message = "A database error occurred. Please try again later.";
        
        ErrorDetail error = new ErrorDetail(
                message,
                "DATABASE_ERROR"
        );
        
        ApiResponse<Object> response = ApiResponse.error(message, error);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    
    /**
     * Handles all other uncaught exceptions.
     * Returns 500 Internal Server Error.
     * This is the catch-all handler that should log full details for debugging
     * but return a safe generic message to the client.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGlobalException(Exception ex) {
        logger.error("Unexpected error occurred", ex);
        
        String message = "An unexpected error occurred. Please try again later.";
        
        ErrorDetail error = new ErrorDetail(
                message,
                "INTERNAL_ERROR"
        );
        
        ApiResponse<Object> response = ApiResponse.error(message, error);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    
    /**
     * Extracts the property name from a constraint violation property path.
     * Example: "updateProduct.productId" -> "productId"
     */
    private String getPropertyName(String propertyPath) {
        if (propertyPath == null) {
            return "unknown";
        }
        String[] parts = propertyPath.split("\\.");
        return parts.length > 0 ? parts[parts.length - 1] : propertyPath;
    }
}

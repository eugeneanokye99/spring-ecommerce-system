package com.shopjoy.exception;

import com.shopjoy.dto.response.ApiResponse;
import com.shopjoy.dto.response.ErrorDetail;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Example test class demonstrating how to test GlobalExceptionHandler.
 * This shows the expected behavior for each exception type.
 */
class GlobalExceptionHandlerTest {
    
    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();
    
    @Test
    void testHandleValidationErrors_ReturnsValidationErrorDetails() {
        // Arrange
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        
        FieldError fieldError = new FieldError("createProductRequest", "productName", 
                null, false, new String[]{"NotBlank"}, null, "must not be blank");
        
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(java.util.List.of(fieldError));
        when(ex.getMessage()).thenReturn("Validation failed");
        
        // Act
        ResponseEntity<ApiResponse<Object>> response = handler.handleValidationErrors(ex);
        
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Validation failed for one or more fields", response.getBody().getMessage());
        assertNotNull(response.getBody().getErrors());
        assertEquals(1, response.getBody().getErrors().size());
        
        ErrorDetail error = response.getBody().getErrors().get(0);
        assertEquals("productName", error.getField());
        assertEquals("must not be blank", error.getMessage());
    }
    
    @Test
    void testHandleResourceNotFound_Returns404() {
        // Arrange
        ResourceNotFoundException ex = new ResourceNotFoundException("Product", "id", 123);
        
        // Act
        ResponseEntity<ApiResponse<Object>> response = handler.handleResourceNotFound(ex);
        
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("Product not found"));
        assertTrue(response.getBody().getMessage().contains("123"));
    }
    
    @Test
    void testHandleDuplicateResource_Returns409() {
        // Arrange
        DuplicateResourceException ex = new DuplicateResourceException("User", "email", "test@example.com");
        
        // Act
        ResponseEntity<ApiResponse<Object>> response = handler.handleDuplicateResource(ex);
        
        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("User already exists"));
        assertTrue(response.getBody().getMessage().contains("test@example.com"));
    }
    
    @Test
    void testHandleBusinessValidation_Returns400() {
        // Arrange
        ValidationException ex = new ValidationException("price", "must not be negative");
        
        // Act
        ResponseEntity<ApiResponse<Object>> response = handler.handleBusinessValidation(ex);
        
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("price"));
    }
    
    @Test
    void testHandleInsufficientStock_Returns400() {
        // Arrange
        InsufficientStockException ex = new InsufficientStockException(123, 100, 50);
        
        // Act
        ResponseEntity<ApiResponse<Object>> response = handler.handleInsufficientStock(ex);
        
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("Insufficient stock"));
        assertTrue(response.getBody().getMessage().contains("123"));
    }
    
    @Test
    void testHandleInvalidOperation_Returns400() {
        // Arrange
        InvalidOperationException ex = new InvalidOperationException("cancel_order", "Order already shipped");
        
        // Act
        ResponseEntity<ApiResponse<Object>> response = handler.handleInvalidOperation(ex);
        
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("cancel_order"));
    }
    
    @Test
    void testHandleInvalidOrderState_Returns400() {
        // Arrange
        InvalidOrderStateException ex = new InvalidOrderStateException(456, "SHIPPED", "cancel");
        
        // Act
        ResponseEntity<ApiResponse<Object>> response = handler.handleInvalidOrderState(ex);
        
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("cancel"));
        assertTrue(response.getBody().getMessage().contains("456"));
    }
    
    @Test
    void testHandleMalformedJson_Returns400() {
        // Arrange
        HttpMessageNotReadableException ex = mock(HttpMessageNotReadableException.class);
        when(ex.getMessage()).thenReturn("JSON parse error");
        
        // Act
        ResponseEntity<ApiResponse<Object>> response = handler.handleMalformedJson(ex);
        
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("Malformed JSON"));
    }
    
    @Test
    void testHandleDataIntegrityViolation_Returns409() {
        // Arrange
        DataIntegrityViolationException ex = new DataIntegrityViolationException("unique constraint violation");
        
        // Act
        ResponseEntity<ApiResponse<Object>> response = handler.handleDataIntegrityViolation(ex);
        
        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
    }
    
    @Test
    void testHandleDatabaseError_Returns500() {
        // Arrange
        DataAccessException ex = mock(DataAccessException.class);
        when(ex.getMessage()).thenReturn("Connection timeout");
        
        // Act
        ResponseEntity<ApiResponse<Object>> response = handler.handleDatabaseError(ex);
        
        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("A database error occurred. Please try again later.", response.getBody().getMessage());
        // Verify internal details are NOT exposed
        assertFalse(response.getBody().getMessage().contains("Connection timeout"));
    }
    
    @Test
    void testHandleGlobalException_Returns500() {
        // Arrange
        Exception ex = new RuntimeException("Internal server error");
        
        // Act
        ResponseEntity<ApiResponse<Object>> response = handler.handleGlobalException(ex);
        
        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("An unexpected error occurred. Please try again later.", response.getBody().getMessage());
        // Verify internal details are NOT exposed
        assertFalse(response.getBody().getMessage().contains("Internal server error"));
    }
    
    @Test
    void testAllResponses_HaveTimestamp() {
        // Arrange
        ResourceNotFoundException ex = new ResourceNotFoundException("Product", "id", 1);
        
        // Act
        ResponseEntity<ApiResponse<Object>> response = handler.handleResourceNotFound(ex);
        
        // Assert
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getTimestamp());
    }
    
    @Test
    void testAllResponses_HaveSuccessFalse() {
        // Test multiple exception types
        ResourceNotFoundException notFoundEx = new ResourceNotFoundException("Product", "id", 1);
        ValidationException validationEx = new ValidationException("Invalid input");
        
        ResponseEntity<ApiResponse<Object>> notFoundResponse = handler.handleResourceNotFound(notFoundEx);
        ResponseEntity<ApiResponse<Object>> validationResponse = handler.handleBusinessValidation(validationEx);
        
        assertFalse(notFoundResponse.getBody().isSuccess());
        assertFalse(validationResponse.getBody().isSuccess());
    }
}

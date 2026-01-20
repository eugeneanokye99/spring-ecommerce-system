package com.shopjoy.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "Detailed error information for validation or business rule violations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetail {
    
    @Schema(description = "Field name that caused the error", example = "email")
    private String field;
    
    @Schema(description = "Human-readable error message", example = "Email must be valid")
    private String message;
    
    @Schema(description = "The value that was rejected", example = "invalid-email")
    private Object rejectedValue;
    
    @Schema(description = "Machine-readable error code", example = "INVALID_FORMAT")
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

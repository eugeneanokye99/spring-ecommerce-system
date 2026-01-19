package com.shopjoy.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Validates that a stock quantity is non-negative.
 * 
 * Usage:
 * <pre>
 * public class UpdateInventoryRequest {
 *     @ValidStock
 *     private Integer stockQuantity;
 * }
 * </pre>
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidStockValidator.class)
@Documented
public @interface ValidStock {
    
    String message() default "Stock quantity must be non-negative";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
    /**
     * Maximum allowed stock quantity (default: Integer.MAX_VALUE)
     */
    int max() default Integer.MAX_VALUE;
}

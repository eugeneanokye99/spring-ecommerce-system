package com.shopjoy.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Validates that a price value is positive and has at most 2 decimal places.
 * 
 * Usage:
 * <pre>
 * public class CreateProductRequest {
 *     @ValidPrice
 *     private Double price;
 * }
 * </pre>
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidPriceValidator.class)
@Documented
public @interface ValidPrice {
    
    String message() default "Price must be positive and have at most 2 decimal places";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
    /**
     * Whether null values are allowed (default: false)
     */
    boolean allowNull() default false;
}

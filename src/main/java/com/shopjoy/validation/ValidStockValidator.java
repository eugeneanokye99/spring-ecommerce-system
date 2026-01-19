package com.shopjoy.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator implementation for @ValidStock annotation.
 * 
 * Validates that:
 * - Value is not null
 * - Value is non-negative (>= 0)
 * - Value does not exceed maximum if specified
 */
public class ValidStockValidator implements ConstraintValidator<ValidStock, Integer> {
    
    private int max;
    
    @Override
    public void initialize(ValidStock constraintAnnotation) {
        this.max = constraintAnnotation.max();
    }
    
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        // Null check
        if (value == null) {
            return false;
        }
        
        // Must be non-negative
        if (value < 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Stock quantity cannot be negative")
                    .addConstraintViolation();
            return false;
        }
        
        // Check maximum
        if (value > max) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    String.format("Stock quantity cannot exceed %d", max))
                    .addConstraintViolation();
            return false;
        }
        
        return true;
    }
}

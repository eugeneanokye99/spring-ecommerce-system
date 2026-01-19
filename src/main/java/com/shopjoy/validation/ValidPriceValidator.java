package com.shopjoy.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

/**
 * Validator implementation for @ValidPrice annotation.
 * 
 * Validates that:
 * - Value is not null (unless allowNull is true)
 * - Value is positive (greater than 0)
 * - Value has at most 2 decimal places
 */
public class ValidPriceValidator implements ConstraintValidator<ValidPrice, Number> {
    
    private boolean allowNull;
    
    @Override
    public void initialize(ValidPrice constraintAnnotation) {
        this.allowNull = constraintAnnotation.allowNull();
    }
    
    @Override
    public boolean isValid(Number value, ConstraintValidatorContext context) {
        // Null check
        if (value == null) {
            return allowNull;
        }
        
        double price = value.doubleValue();
        
        // Must be positive
        if (price <= 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Price must be greater than 0")
                    .addConstraintViolation();
            return false;
        }
        
        // Check decimal places (at most 2)
        BigDecimal bd = BigDecimal.valueOf(price);
        int decimalPlaces = bd.scale();
        
        if (decimalPlaces > 2) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Price must have at most 2 decimal places")
                    .addConstraintViolation();
            return false;
        }
        
        return true;
    }
}

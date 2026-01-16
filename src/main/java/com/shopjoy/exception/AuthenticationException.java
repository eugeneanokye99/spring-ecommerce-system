package com.shopjoy.exception;

public class AuthenticationException extends BusinessException {
    
    public AuthenticationException(String message) {
        super(message, "AUTHENTICATION_FAILED");
    }
    
    public AuthenticationException() {
        super("Invalid username or password", "AUTHENTICATION_FAILED");
    }
}

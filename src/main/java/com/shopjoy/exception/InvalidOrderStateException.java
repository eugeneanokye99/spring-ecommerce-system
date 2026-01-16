package com.shopjoy.exception;

public class InvalidOrderStateException extends BusinessException {
    
    public InvalidOrderStateException(int orderId, String currentState, String attemptedAction) {
        super(String.format("Cannot %s order %d in state: %s", attemptedAction, orderId, currentState), 
                "INVALID_ORDER_STATE");
    }
    
    public InvalidOrderStateException(String message) {
        super(message, "INVALID_ORDER_STATE");
    }
}

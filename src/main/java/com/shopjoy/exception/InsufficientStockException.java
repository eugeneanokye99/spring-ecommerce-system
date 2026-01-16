package com.shopjoy.exception;

public class InsufficientStockException extends BusinessException {
    
    private final int productId;
    private final int requestedQuantity;
    private final int availableQuantity;
    
    public InsufficientStockException(int productId, int requestedQuantity, int availableQuantity) {
        super(String.format("Insufficient stock for product ID %d. Requested: %d, Available: %d", 
                productId, requestedQuantity, availableQuantity), "INSUFFICIENT_STOCK");
        this.productId = productId;
        this.requestedQuantity = requestedQuantity;
        this.availableQuantity = availableQuantity;
    }
    
    public int getProductId() {
        return productId;
    }
    
    public int getRequestedQuantity() {
        return requestedQuantity;
    }
    
    public int getAvailableQuantity() {
        return availableQuantity;
    }
}

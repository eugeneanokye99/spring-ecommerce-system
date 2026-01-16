package com.shopjoy.dto.response;

import java.time.LocalDateTime;

/**
 * Response DTO for Inventory.
 */
public class InventoryResponse {
    
    private int inventoryId;
    private int productId;
    private String productName;
    private int quantityInStock;
    private int reorderLevel;
    private LocalDateTime lastRestocked;
    
    public InventoryResponse() {
    }
    
    public InventoryResponse(int inventoryId, int productId, String productName, 
                            int quantityInStock, int reorderLevel, LocalDateTime lastRestocked) {
        this.inventoryId = inventoryId;
        this.productId = productId;
        this.productName = productName;
        this.quantityInStock = quantityInStock;
        this.reorderLevel = reorderLevel;
        this.lastRestocked = lastRestocked;
    }
    
    public int getInventoryId() {
        return inventoryId;
    }
    
    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }
    
    public int getProductId() {
        return productId;
    }
    
    public void setProductId(int productId) {
        this.productId = productId;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public int getQuantityInStock() {
        return quantityInStock;
    }
    
    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }
    
    public int getReorderLevel() {
        return reorderLevel;
    }
    
    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }
    
    public LocalDateTime getLastRestocked() {
        return lastRestocked;
    }
    
    public void setLastRestocked(LocalDateTime lastRestocked) {
        this.lastRestocked = lastRestocked;
    }
    
    public boolean needsReorder() {
        return quantityInStock <= reorderLevel;
    }
}

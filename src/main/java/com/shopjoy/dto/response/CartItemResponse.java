package com.shopjoy.dto.response;

import java.time.LocalDateTime;

/**
 * Response DTO for CartItem.
 */
public class CartItemResponse {
    
    private int cartItemId;
    private int userId;
    private int productId;
    private String productName;
    private double productPrice;
    private int quantity;
    private LocalDateTime createdAt;
    
    public CartItemResponse() {
    }
    
    public CartItemResponse(int cartItemId, int userId, int productId, String productName, 
                           double productPrice, int quantity, LocalDateTime createdAt) {
        this.cartItemId = cartItemId;
        this.userId = userId;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.createdAt = createdAt;
    }
    
    public int getCartItemId() {
        return cartItemId;
    }
    
    public void setCartItemId(int cartItemId) {
        this.cartItemId = cartItemId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
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
    
    public double getProductPrice() {
        return productPrice;
    }
    
    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public double getSubtotal() {
        return productPrice * quantity;
    }
}

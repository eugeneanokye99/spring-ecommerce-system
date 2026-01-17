package com.shopjoy.service;

import com.shopjoy.dto.request.AddToCartRequest;
import com.shopjoy.dto.response.CartItemResponse;
import com.shopjoy.exception.InsufficientStockException;
import com.shopjoy.exception.ResourceNotFoundException;
import com.shopjoy.exception.ValidationException;

import java.util.List;

/**
 * Service interface for Shopping Cart operations.
 * Handles cart management for users.
 */
public interface CartService {
    
    /**
     * Adds a product to the user's cart.
     * If product already exists in cart, updates the quantity.
     * 
     * @param request the add to cart request
     * @return the cart item response
     * @throws ResourceNotFoundException if user or product not found
     * @throws ValidationException if quantity is invalid
     * @throws InsufficientStockException if requested quantity exceeds available stock
     */
    CartItemResponse addToCart(AddToCartRequest request);
    
    /**
     * Updates the quantity of an item in the cart.
     * 
     * @param cartItemId the cart item ID
     * @param newQuantity the new quantity
     * @return the updated cart item response
     * @throws ResourceNotFoundException if cart item not found
     * @throws ValidationException if quantity is invalid
     * @throws InsufficientStockException if requested quantity exceeds available stock
     */
    CartItemResponse updateCartItemQuantity(Integer cartItemId, int newQuantity);
    
    /**
     * Removes an item from the cart.
     * 
     * @param cartItemId the cart item ID
     * @throws ResourceNotFoundException if cart item not found
     */
    void removeFromCart(Integer cartItemId);
    
    /**
     * Retrieves all items in a user's cart.
     * 
     * @param userId the user ID
     * @return list of cart item responses
     */
    List<CartItemResponse> getCartItems(Integer userId);
    
    /**
     * Clears all items from a user's cart.
     * Typically called after successful order creation.
     * 
     * @param userId the user ID
     */
    void clearCart(Integer userId);
    
    /**
     * Calculates the total value of items in the cart.
     * 
     * @param userId the user ID
     * @return the cart total
     */
    double getCartTotal(Integer userId);
    
    /**
     * Counts the number of items in the cart.
     * 
     * @param userId the user ID
     * @return the item count
     */
    int getCartItemCount(Integer userId);
}

package com.shopjoy.dto.mapper;

import com.shopjoy.dto.request.AddToCartRequest;
import com.shopjoy.dto.response.CartItemResponse;
import com.shopjoy.entity.CartItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper for CartItem entity and DTOs.
 */
public class CartItemMapper {
    
    public static CartItem toCartItem(AddToCartRequest request) {
        if (request == null) {
            return null;
        }
        
        CartItem cartItem = new CartItem();
        cartItem.setUserId(request.getUserId());
        cartItem.setProductId(request.getProductId());
        cartItem.setQuantity(request.getQuantity());
        
        return cartItem;
    }
    
    public static CartItemResponse toCartItemResponse(CartItem cartItem) {
        if (cartItem == null) {
            return null;
        }
        
        // Product name and price will be populated by service layer
        return new CartItemResponse(
            cartItem.getCartItemId(),
            cartItem.getUserId(),
            cartItem.getProductId(),
            null, // productName - set by service
            0.0,  // productPrice - set by service
            cartItem.getQuantity(),
            cartItem.getCreatedAt()
        );
    }
    
    public static List<CartItemResponse> toCartItemResponseList(List<CartItem> cartItems) {
        if (cartItems == null) {
            return null;
        }
        
        List<CartItemResponse> responses = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            responses.add(toCartItemResponse(cartItem));
        }
        return responses;
    }
}

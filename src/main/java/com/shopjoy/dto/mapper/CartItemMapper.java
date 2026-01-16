package com.shopjoy.dto.mapper;

import com.shopjoy.dto.response.CartItemResponse;
import com.shopjoy.entity.CartItem;


/**
 * Mapper for CartItem entity and DTOs.
 */
public class CartItemMapper {


    /**
     * To cart item response.
     *
     * @param cartItem the cart item
     * @return the cart item response
     */
    public static CartItemResponse toCartItemResponse(CartItem cartItem) {
        if (cartItem == null) {
            return null;
        }
        
        return new CartItemResponse(
            cartItem.getCartItemId(),
            cartItem.getUserId(),
            cartItem.getProductId(),
            null,
            0.0,
            cartItem.getQuantity(),
            cartItem.getCreatedAt()
        );
    }
    

}

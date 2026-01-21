package com.shopjoy.graphql.resolver.mutation;

import com.shopjoy.dto.request.AddToCartRequest;
import com.shopjoy.dto.response.CartItemResponse;
import com.shopjoy.service.CartService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class CartMutationResolver {

    private final CartService cartService;

    public CartMutationResolver(CartService cartService) {
        this.cartService = cartService;
    }

    @MutationMapping
    public CartItemResponse addToCart(
            @Argument Long userId,
            @Argument Long productId,
            @Argument Integer quantity
    ) {
        var request = AddToCartRequest.builder()
                .userId(userId.intValue())
                .productId(productId.intValue())
                .quantity(quantity)
                .build();
        return cartService.addToCart(request);
    }

    @MutationMapping
    public Boolean removeFromCart(@Argument Long cartItemId) {
        cartService.removeFromCart(cartItemId.intValue());
        return true;
    }
}

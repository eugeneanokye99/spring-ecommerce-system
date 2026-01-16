package com.shopjoy.controller;

import com.shopjoy.dto.mapper.CartItemMapper;
import com.shopjoy.dto.request.AddToCartRequest;
import com.shopjoy.dto.response.ApiResponse;
import com.shopjoy.dto.response.CartItemResponse;
import com.shopjoy.entity.CartItem;
import com.shopjoy.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/items")
    public ResponseEntity<ApiResponse<CartItemResponse>> addToCart(@Valid @RequestBody AddToCartRequest request) {
        CartItem cartItem = cartService.addToCart(
                request.getUserId(),
                request.getProductId(),
                request.getQuantity()
        );
        CartItemResponse response = CartItemMapper.toCartItemResponse(cartItem);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Item added to cart successfully"));
    }

    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<ApiResponse<CartItemResponse>> updateCartItemQuantity(
            @PathVariable Integer cartItemId,
            @RequestParam Integer quantity) {
        CartItem updatedItem = cartService.updateCartItemQuantity(cartItemId, quantity);
        CartItemResponse response = CartItemMapper.toCartItemResponse(updatedItem);
        return ResponseEntity.ok(ApiResponse.success(response, "Cart item quantity updated successfully"));
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<ApiResponse<Void>> removeFromCart(@PathVariable Integer cartItemId) {
        cartService.removeFromCart(cartItemId);
        return ResponseEntity.ok(ApiResponse.success(null, "Item removed from cart successfully"));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<CartItemResponse>>> getCartItems(@PathVariable Integer userId) {
        List<CartItem> cartItems = cartService.getCartItems(userId);
        List<CartItemResponse> responses = cartItems.stream()
                .map(CartItemMapper::toCartItemResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responses, "Cart items retrieved successfully"));
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Void>> clearCart(@PathVariable Integer userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok(ApiResponse.success(null, "Cart cleared successfully"));
    }

    @GetMapping("/user/{userId}/total")
    public ResponseEntity<ApiResponse<Double>> getCartTotal(@PathVariable Integer userId) {
        double total = cartService.getCartTotal(userId);
        return ResponseEntity.ok(ApiResponse.success(total, "Cart total calculated successfully"));
    }

    @GetMapping("/user/{userId}/count")
    public ResponseEntity<ApiResponse<Integer>> getCartItemCount(@PathVariable Integer userId) {
        int count = cartService.getCartItemCount(userId);
        return ResponseEntity.ok(ApiResponse.success(count, "Cart item count retrieved successfully"));
    }
}

package com.shopjoy.service.impl;

import com.shopjoy.entity.CartItem;
import com.shopjoy.entity.Product;
import com.shopjoy.exception.InsufficientStockException;
import com.shopjoy.exception.ResourceNotFoundException;
import com.shopjoy.exception.ValidationException;
import com.shopjoy.repository.CartItemRepository;
import com.shopjoy.service.CartService;
import com.shopjoy.service.InventoryService;
import com.shopjoy.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CartServiceImpl implements CartService {
    
    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);
    
    private final CartItemRepository cartItemRepository;
    private final ProductService productService;
    private final InventoryService inventoryService;
    
    public CartServiceImpl(CartItemRepository cartItemRepository,
                          ProductService productService,
                          InventoryService inventoryService) {
        this.cartItemRepository = cartItemRepository;
        this.productService = productService;
        this.inventoryService = inventoryService;
    }
    
    @Override
    @Transactional(readOnly = false)
    public CartItem addToCart(Integer userId, Integer productId, int quantity) {
        logger.info("Adding product {} to cart for user {}", productId, userId);
        
        if (quantity <= 0) {
            throw new ValidationException("quantity", "must be positive");
        }
        
        productService.getProductById(productId);
        
        if (!inventoryService.hasAvailableStock(productId, quantity)) {
            throw new InsufficientStockException(productId, quantity, 0);
        }
        
        Optional<CartItem> existingItem = cartItemRepository.findByUserAndProduct(userId, productId);
        
        if (existingItem.isPresent()) {
            CartItem cartItem = existingItem.get();
            int newQuantity = cartItem.getQuantity() + quantity;
            
            if (!inventoryService.hasAvailableStock(productId, newQuantity)) {
                throw new InsufficientStockException(productId, newQuantity, 0);
            }
            
            cartItem.setQuantity(newQuantity);
            return cartItemRepository.update(cartItem);
        } else {
            CartItem cartItem = CartItem.builder()
                    .userId(userId)
                    .productId(productId)
                    .quantity(quantity)
                    .createdAt(LocalDateTime.now())
                    .build();
            
            return cartItemRepository.save(cartItem);
        }
    }
    
    @Override
    @Transactional(readOnly = false)
    public CartItem updateCartItemQuantity(Integer cartItemId, int newQuantity) {
        logger.info("Updating cart item {} quantity to {}", cartItemId, newQuantity);
        
        if (newQuantity <= 0) {
            throw new ValidationException("quantity", "must be positive");
        }
        
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", cartItemId));
        
        if (!inventoryService.hasAvailableStock(cartItem.getProductId(), newQuantity)) {
            throw new InsufficientStockException(cartItem.getProductId(), newQuantity, 0);
        }
        
        cartItem.setQuantity(newQuantity);
        return cartItemRepository.update(cartItem);
    }
    
    @Override
    @Transactional(readOnly = false)
    public void removeFromCart(Integer cartItemId) {
        logger.info("Removing cart item {}", cartItemId);
        
        if (!cartItemRepository.existsById(cartItemId)) {
            throw new ResourceNotFoundException("CartItem", "id", cartItemId);
        }
        
        cartItemRepository.delete(cartItemId);
    }
    
    @Override
    public List<CartItem> getCartItems(Integer userId) {
        return cartItemRepository.findByUserId(userId);
    }
    
    @Override
    @Transactional(readOnly = false)
    public void clearCart(Integer userId) {
        logger.info("Clearing cart for user {}", userId);
        cartItemRepository.clearCart(userId);
    }
    
    @Override
    public double getCartTotal(Integer userId) {
        List<CartItem> items = getCartItems(userId);
        
        return items.stream()
                .mapToDouble(item -> {
                    Product product = productService.getProductById(item.getProductId());
                    return product.getPrice() * item.getQuantity();
                })
                .sum();
    }
    
    @Override
    public int getCartItemCount(Integer userId) {
        List<CartItem> items = getCartItems(userId);
        return items.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
}

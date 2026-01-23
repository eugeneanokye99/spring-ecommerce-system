import api from './api';

// POST /api/v1/cart/items - Add item to cart
export const addToCart = (cartItemData) => api.post('/cart/items', cartItemData);

// PUT /api/v1/cart/items/{cartItemId}?quantity={quantity} - Update cart item quantity
export const updateCartItemQuantity = (cartItemId, quantity) =>
    api.put(`/cart/items/${cartItemId}`, null, { params: { quantity } });

// DELETE /api/v1/cart/items/{cartItemId} - Remove item from cart
export const removeFromCart = (cartItemId) => api.delete(`/cart/items/${cartItemId}`);

// GET /api/v1/cart/user/{userId} - Get cart items
export const getCartItems = (userId) => api.get(`/cart/user/${userId}`);

// DELETE /api/v1/cart/user/{userId} - Clear cart
export const clearCart = (userId) => api.delete(`/cart/user/${userId}`);

// GET /api/v1/cart/user/{userId}/total - Get cart total
export const getCartTotal = (userId) => api.get(`/cart/user/${userId}/total`);

// GET /api/v1/cart/user/{userId}/count - Get cart item count
export const getCartItemCount = (userId) => api.get(`/cart/user/${userId}/count`);

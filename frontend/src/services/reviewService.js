import api from './api';

// POST /api/v1/reviews - Create review
export const createReview = (reviewData) => api.post('/reviews', reviewData);

// GET /api/v1/reviews - Get all reviews
export const getAllReviews = () => api.get('/reviews');

// GET /api/v1/reviews/{id} - Get review by ID
export const getReviewById = (id) => api.get(`/reviews/${id}`);

// GET /api/v1/reviews/product/{productId} - Get reviews by product
export const getReviewsByProduct = (productId) => api.get(`/reviews/product/${productId}`);

// GET /api/v1/reviews/user/{userId} - Get reviews by user
export const getReviewsByUser = (userId) => api.get(`/reviews/user/${userId}`);

// GET /api/v1/reviews/product/{productId}/rating/{rating} - Get reviews by rating
export const getReviewsByRating = (productId, rating) =>
    api.get(`/reviews/product/${productId}/rating/${rating}`);

// GET /api/v1/reviews/product/{productId}/average-rating - Get average rating
export const getAverageRating = (productId) => api.get(`/reviews/product/${productId}/average-rating`);

// PUT /api/v1/reviews/{id} - Update review
export const updateReview = (id, reviewData) => api.put(`/reviews/${id}`, reviewData);

// PATCH /api/v1/reviews/{id}/helpful - Mark review as helpful
export const markReviewAsHelpful = (id) => api.patch(`/reviews/${id}/helpful`);

// DELETE /api/v1/reviews/{id} - Delete review
export const deleteReview = (id) => api.delete(`/reviews/${id}`);

package com.shopjoy.service;

import com.shopjoy.entity.Review;
import com.shopjoy.exception.BusinessException;
import com.shopjoy.exception.ResourceNotFoundException;
import com.shopjoy.exception.ValidationException;

import java.util.List;

/**
 * Service interface for Product Review operations.
 * Handles review creation, moderation, and queries.
 */
public interface ReviewService {
    
    /**
     * Creates a new product review.
     * Validates that user has purchased the product if verified purchase flag is set.
     * 
     * @param review the review to create
     * @return the created review with generated ID
     * @throws ResourceNotFoundException if product or user not found
     * @throws ValidationException if review data is invalid
     * @throws BusinessException if user hasn't purchased the product
     */
    Review createReview(Review review);
    
    /**
     * Retrieves a review by its ID.
     * 
     * @param reviewId the review ID
     * @return the review
     * @throws ResourceNotFoundException if review not found
     */
    Review getReviewById(Integer reviewId);
    
    /**
     * Retrieves all reviews for a product.
     * 
     * @param productId the product ID
     * @return list of reviews
     */
    List<Review> getReviewsByProduct(Integer productId);
    
    /**
     * Retrieves all reviews by a specific user.
     * 
     * @param userId the user ID
     * @return list of reviews
     */
    List<Review> getReviewsByUser(Integer userId);
    
    /**
     * Retrieves reviews for a product filtered by rating.
     * 
     * @param productId the product ID
     * @param rating the rating (1-5)
     * @return list of reviews with the specified rating
     */
    List<Review> getReviewsByRating(Integer productId, int rating);
    
    /**
     * Calculates the average rating for a product.
     * 
     * @param productId the product ID
     * @return the average rating (0.0 if no reviews)
     */
    double getAverageRating(Integer productId);
    
    /**
     * Updates an existing review.
     * Only the review author can update their review.
     * 
     * @param review the review with updated information
     * @return the updated review
     * @throws ResourceNotFoundException if review not found
     * @throws ValidationException if review data is invalid
     * @throws BusinessException if attempting to update someone else's review
     */
    Review updateReview(Review review);
    
    /**
     * Deletes a review.
     * 
     * @param reviewId the review ID
     * @throws ResourceNotFoundException if review not found
     */
    void deleteReview(Integer reviewId);
    
    /**
     * Marks a review as helpful (increments helpful count).
     * 
     * @param reviewId the review ID
     * @return the updated review
     * @throws ResourceNotFoundException if review not found
     */
    Review markReviewAsHelpful(Integer reviewId);
}

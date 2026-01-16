package com.shopjoy.dto.mapper;

import com.shopjoy.dto.request.CreateReviewRequest;
import com.shopjoy.dto.request.UpdateReviewRequest;
import com.shopjoy.dto.response.ReviewResponse;
import com.shopjoy.entity.Review;

/**
 * Mapper for Review entity and DTOs.
 */
public class ReviewMapper {
    
    public static Review toReview(CreateReviewRequest request) {
        if (request == null) {
            return null;
        }
        
        Review review = new Review();
        review.setUserId(request.getUserId());
        review.setProductId(request.getProductId());
        review.setRating(request.getRating());
        review.setTitle(request.getTitle());
        review.setComment(request.getComment());
        
        return review;
    }
    
    public static ReviewResponse toReviewResponse(Review review) {
        if (review == null) {
            return null;
        }
        
        // Username and product name will be populated by service layer
        return new ReviewResponse(
            review.getReviewId(),
            review.getUserId(),
            null, // userName - set by service
            review.getProductId(),
            null, // productName - set by service
            review.getRating(),
            review.getTitle(),
            review.getComment(),
            review.getCreatedAt()
        );
    }
    
    public static void updateReviewFromRequest(Review review, UpdateReviewRequest request) {
        if (review == null || request == null) {
            return;
        }
        
        if (request.getRating() != null) {
            review.setRating(request.getRating());
        }
        
        if (request.getTitle() != null) {
            review.setTitle(request.getTitle());
        }
        
        if (request.getComment() != null) {
            review.setComment(request.getComment());
        }
    }
}

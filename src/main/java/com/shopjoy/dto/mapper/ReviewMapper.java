package com.shopjoy.dto.mapper;

import com.shopjoy.dto.request.CreateReviewRequest;
import com.shopjoy.dto.request.UpdateReviewRequest;
import com.shopjoy.dto.response.ReviewResponse;
import com.shopjoy.entity.Review;

import java.util.ArrayList;
import java.util.List;

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
        
        // User name and product name will be populated by service layer
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
    
    public static List<ReviewResponse> toReviewResponseList(List<Review> reviews) {
        if (reviews == null) {
            return null;
        }
        
        List<ReviewResponse> responses = new ArrayList<>();
        for (Review review : reviews) {
            responses.add(toReviewResponse(review));
        }
        return responses;
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

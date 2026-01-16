package com.shopjoy.service.impl;

import com.shopjoy.entity.Review;
import com.shopjoy.exception.BusinessException;
import com.shopjoy.exception.ResourceNotFoundException;
import com.shopjoy.exception.ValidationException;
import com.shopjoy.repository.OrderRepository;
import com.shopjoy.repository.ReviewRepository;
import com.shopjoy.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {
    
    private static final Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);
    
    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    
    public ReviewServiceImpl(ReviewRepository reviewRepository, OrderRepository orderRepository) {
        this.reviewRepository = reviewRepository;
        this.orderRepository = orderRepository;
    }
    
    @Override
    @Transactional(readOnly = false)
    public Review createReview(Review review) {
        logger.info("Creating review for product {} by user {}", 
                review.getProductId(), review.getUserId());
        
        validateReviewData(review);
        
        if (reviewRepository.hasUserReviewedProduct(review.getUserId(), review.getProductId())) {
            throw new BusinessException("User has already reviewed this product");
        }
        
        boolean hasPurchased = orderRepository.hasUserPurchasedProduct(
                review.getUserId(), review.getProductId());
        
        if (!hasPurchased) {
            logger.warn("User {} attempting to review product {} without purchase", 
                    review.getUserId(), review.getProductId());
        }
        
        review.setCreatedAt(LocalDateTime.now());
        review.setHelpfulCount(0);
        
        Review createdReview = reviewRepository.save(review);
        logger.info("Successfully created review ID: {}", createdReview.getReviewId());
        
        return createdReview;
    }
    
    @Override
    public Review getReviewById(Integer reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", reviewId));
    }
    
    @Override
    public List<Review> getReviewsByProduct(Integer productId) {
        return reviewRepository.findByProductId(productId);
    }
    
    @Override
    public List<Review> getReviewsByUser(Integer userId) {
        return reviewRepository.findByUserId(userId);
    }
    
    @Override
    @Transactional(readOnly = false)
    public Review updateReview(Review review) {
        logger.info("Updating review ID: {}", review.getReviewId());
        
        getReviewById(review.getReviewId());
        validateReviewData(review);
        
        return reviewRepository.update(review);
    }
    
    @Override
    @Transactional(readOnly = false)
    public void deleteReview(Integer reviewId) {
        logger.info("Deleting review ID: {}", reviewId);
        
        if (!reviewRepository.existsById(reviewId)) {
            throw new ResourceNotFoundException("Review", "id", reviewId);
        }
        
        reviewRepository.delete(reviewId);
    }
    
    @Override
    public List<Review> getReviewsByRating(Integer productId, int rating) {
        if (rating < 1 || rating > 5) {
            throw new ValidationException("rating", "must be between 1 and 5");
        }
        return reviewRepository.findByProductId(productId).stream()
                .filter(review -> review.getRating() == rating)
                .toList();
    }
    
    @Override
    public double getAverageRating(Integer productId) {
        return reviewRepository.getAverageRating(productId);
    }
    
    @Override
    @Transactional(readOnly = false)
    public Review markReviewAsHelpful(Integer reviewId) {
        logger.debug("Incrementing helpful count for review {}", reviewId);
        
        if (!reviewRepository.existsById(reviewId)) {
            throw new ResourceNotFoundException("Review", "id", reviewId);
        }
        
        reviewRepository.incrementHelpfulCount(reviewId);
        return getReviewById(reviewId);
    }
    
    private void validateReviewData(Review review) {
        if (review == null) {
            throw new ValidationException("Review data cannot be null");
        }
        
        if (review.getUserId() <= 0) {
            throw new ValidationException("userId", "is required");
        }
        
        if (review.getProductId() <= 0) {
            throw new ValidationException("productId", "is required");
        }
        
        if (review.getRating() < 1 || review.getRating() > 5) {
            throw new ValidationException("rating", "must be between 1 and 5");
        }
        
        if (review.getComment() != null && review.getComment().length() > 1000) {
            throw new ValidationException("comment", "must not exceed 1000 characters");
        }
    }
}

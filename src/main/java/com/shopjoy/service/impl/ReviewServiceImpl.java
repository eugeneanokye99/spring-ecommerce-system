package com.shopjoy.service.impl;

import com.shopjoy.dto.mapper.ReviewMapper;
import com.shopjoy.dto.request.CreateReviewRequest;
import com.shopjoy.dto.request.UpdateReviewRequest;
import com.shopjoy.dto.response.ReviewResponse;
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
import java.util.stream.Collectors;

/**
 * The type Review service.
 */
@Service
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {
    
    private static final Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);
    
    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;

    /**
     * Instantiates a new Review service.
     *
     * @param reviewRepository the review repository
     * @param orderRepository  the order repository
     */
    public ReviewServiceImpl(ReviewRepository reviewRepository, OrderRepository orderRepository) {
        this.reviewRepository = reviewRepository;
        this.orderRepository = orderRepository;
    }
    
    @Override
    @Transactional()
    public ReviewResponse createReview(CreateReviewRequest request) {
        logger.info("Creating review for product {} by user {}", 
                request.getProductId(), request.getUserId());
        
        Review review = ReviewMapper.toReview(request);
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
        
        return ReviewMapper.toReviewResponse(createdReview);
    }
    
    @Override
    public ReviewResponse getReviewById(Integer reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", reviewId));
        return ReviewMapper.toReviewResponse(review);
    }
    
    @Override
    public List<ReviewResponse> getReviewsByProduct(Integer productId) {
        return reviewRepository.findByProductId(productId).stream()
                .map(ReviewMapper::toReviewResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ReviewResponse> getReviewsByUser(Integer userId) {
        return reviewRepository.findByUserId(userId).stream()
                .map(ReviewMapper::toReviewResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional()
    public ReviewResponse updateReview(Integer reviewId, UpdateReviewRequest request) {
        logger.info("Updating review ID: {}", reviewId);
        
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", reviewId));
        
        ReviewMapper.updateReviewFromRequest(review, request);
        validateReviewData(review);
        
        Review updatedReview = reviewRepository.update(review);
        return ReviewMapper.toReviewResponse(updatedReview);
    }
    
    @Override
    @Transactional()
    public void deleteReview(Integer reviewId) {
        logger.info("Deleting review ID: {}", reviewId);
        
        if (!reviewRepository.existsById(reviewId)) {
            throw new ResourceNotFoundException("Review", "id", reviewId);
        }
        
        reviewRepository.delete(reviewId);
    }
    
    @Override
    public List<ReviewResponse> getReviewsByRating(Integer productId, int rating) {
        if (rating < 1 || rating > 5) {
            throw new ValidationException("rating", "must be between 1 and 5");
        }
        return reviewRepository.findByProductId(productId).stream()
                .filter(review -> review.getRating() == rating)
                .map(ReviewMapper::toReviewResponse)
                .toList();
    }
    
    @Override
    public double getAverageRating(Integer productId) {
        return reviewRepository.getAverageRating(productId);
    }
    
    @Override
    @Transactional()
    public ReviewResponse markReviewAsHelpful(Integer reviewId) {
        logger.debug("Incrementing helpful count for review {}", reviewId);
        
        if (!reviewRepository.existsById(reviewId)) {
            throw new ResourceNotFoundException("Review", "id", reviewId);
        }
        
        reviewRepository.incrementHelpfulCount(reviewId);
        Review review = reviewRepository.findById(reviewId).orElseThrow();
        return ReviewMapper.toReviewResponse(review);
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

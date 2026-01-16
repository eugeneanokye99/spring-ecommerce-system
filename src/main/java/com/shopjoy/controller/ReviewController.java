package com.shopjoy.controller;

import com.shopjoy.dto.mapper.ReviewMapper;
import com.shopjoy.dto.request.CreateReviewRequest;
import com.shopjoy.dto.request.UpdateReviewRequest;
import com.shopjoy.dto.response.ApiResponse;
import com.shopjoy.dto.response.ReviewResponse;
import com.shopjoy.entity.Review;
import com.shopjoy.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(@Valid @RequestBody CreateReviewRequest request) {
        Review review = ReviewMapper.toReview(request);
        Review createdReview = reviewService.createReview(review);
        ReviewResponse response = ReviewMapper.toReviewResponse(createdReview);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Review created successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewResponse>> getReviewById(@PathVariable Integer id) {
        Review review = reviewService.getReviewById(id);
        ReviewResponse response = ReviewMapper.toReviewResponse(review);
        return ResponseEntity.ok(ApiResponse.success(response, "Review retrieved successfully"));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getReviewsByProduct(@PathVariable Integer productId) {
        List<Review> reviews = reviewService.getReviewsByProduct(productId);
        List<ReviewResponse> responses = reviews.stream()
                .map(ReviewMapper::toReviewResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responses, "Product reviews retrieved successfully"));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getReviewsByUser(@PathVariable Integer userId) {
        List<Review> reviews = reviewService.getReviewsByUser(userId);
        List<ReviewResponse> responses = reviews.stream()
                .map(ReviewMapper::toReviewResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responses, "User reviews retrieved successfully"));
    }

    @GetMapping("/product/{productId}/rating/{rating}")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getReviewsByRating(
            @PathVariable Integer productId,
            @PathVariable Integer rating) {
        List<Review> reviews = reviewService.getReviewsByRating(productId, rating);
        List<ReviewResponse> responses = reviews.stream()
                .map(ReviewMapper::toReviewResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responses, "Reviews by rating retrieved successfully"));
    }

    @GetMapping("/product/{productId}/average-rating")
    public ResponseEntity<ApiResponse<Double>> getAverageRating(@PathVariable Integer productId) {
        double averageRating = reviewService.getAverageRating(productId);
        return ResponseEntity.ok(ApiResponse.success(averageRating, "Average rating calculated successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewResponse>> updateReview(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateReviewRequest request) {
        Review review = reviewService.getReviewById(id);
        ReviewMapper.updateReviewFromRequest(review, request);
        Review updatedReview = reviewService.updateReview(review);
        ReviewResponse response = ReviewMapper.toReviewResponse(updatedReview);
        return ResponseEntity.ok(ApiResponse.success(response, "Review updated successfully"));
    }

    @PatchMapping("/{id}/helpful")
    public ResponseEntity<ApiResponse<Void>> markReviewAsHelpful(@PathVariable Integer id) {
        reviewService.markReviewAsHelpful(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Review marked as helpful successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable Integer id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Review deleted successfully"));
    }
}

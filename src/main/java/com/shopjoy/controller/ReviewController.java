package com.shopjoy.controller;

import com.shopjoy.dto.request.CreateReviewRequest;
import com.shopjoy.dto.request.UpdateReviewRequest;
import com.shopjoy.dto.response.ApiResponse;
import com.shopjoy.dto.response.ReviewResponse;
import com.shopjoy.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Product Review management.
 * Base path: /api/v1/reviews
 * THIN CONTROLLER: Only handles HTTP concerns. All business logic and DTO↔Entity mapping done by services.
 */
@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(
            @Valid @RequestBody CreateReviewRequest request) {
        ReviewResponse response = reviewService.createReview(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Review created successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewResponse>> getReviewById(@PathVariable Integer id) {
        ReviewResponse response = reviewService.getReviewById(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Review retrieved successfully"));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getReviewsByProduct(@PathVariable Integer productId) {
        List<ReviewResponse> response = reviewService.getReviewsByProduct(productId);
        return ResponseEntity.ok(ApiResponse.success(response, "Product reviews retrieved successfully"));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getReviewsByUser(@PathVariable Integer userId) {
        List<ReviewResponse> response = reviewService.getReviewsByUser(userId);
        return ResponseEntity.ok(ApiResponse.success(response, "User reviews retrieved successfully"));
    }

    @GetMapping("/product/{productId}/rating/{rating}")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getReviewsByRating(
            @PathVariable Integer productId,
            @PathVariable Integer rating) {
        List<ReviewResponse> response = reviewService.getReviewsByRating(productId, rating);
        return ResponseEntity.ok(ApiResponse.success(response, "Reviews by rating retrieved successfully"));
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
        ReviewResponse response = reviewService.updateReview(id, request);
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

package com.shopjoy.controller;

import com.shopjoy.dto.request.CreateReviewRequest;
import com.shopjoy.dto.request.UpdateReviewRequest;
import com.shopjoy.dto.response.ApiResponse;
import com.shopjoy.dto.response.ReviewResponse;
import com.shopjoy.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Product Reviews", description = "APIs for managing product reviews including creating, rating, and querying customer reviews")
@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Operation(
            summary = "Create product review",
            description = "Creates a new customer review for a product with rating and comment"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Review created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReviewResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid review data",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Product or user not found",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(
            @Valid @RequestBody CreateReviewRequest request) {
        ReviewResponse response = reviewService.createReview(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Review created successfully"));
    }

    @Operation(
            summary = "Get review by ID",
            description = "Retrieves a specific review by its unique identifier"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Review retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReviewResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Review not found",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewResponse>> getReviewById(
            @Parameter(description = "Review unique identifier", required = true, example = "1")
            @PathVariable Integer id) {
        ReviewResponse response = reviewService.getReviewById(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Review retrieved successfully"));
    }

    @Operation(
            summary = "Get reviews by product",
            description = "Retrieves all reviews for a specific product"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Product reviews retrieved successfully",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Product not found",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getReviewsByProduct(
            @Parameter(description = "Product unique identifier", required = true, example = "1")
            @PathVariable Integer productId) {
        List<ReviewResponse> response = reviewService.getReviewsByProduct(productId);
        return ResponseEntity.ok(ApiResponse.success(response, "Product reviews retrieved successfully"));
    }

    @Operation(
            summary = "Get reviews by user",
            description = "Retrieves all reviews written by a specific user"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "User reviews retrieved successfully",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getReviewsByUser(
            @Parameter(description = "User unique identifier", required = true, example = "1")
            @PathVariable Integer userId) {
        List<ReviewResponse> response = reviewService.getReviewsByUser(userId);
        return ResponseEntity.ok(ApiResponse.success(response, "User reviews retrieved successfully"));
    }

    @Operation(
            summary = "Get reviews by rating",
            description = "Retrieves all reviews for a product filtered by specific rating (1-5 stars)"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Reviews by rating retrieved successfully",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Product not found",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/product/{productId}/rating/{rating}")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getReviewsByRating(
            @Parameter(description = "Product unique identifier", required = true, example = "1")
            @PathVariable Integer productId,
            @Parameter(description = "Rating value (1-5)", required = true, example = "5")
            @PathVariable Integer rating) {
        List<ReviewResponse> response = reviewService.getReviewsByRating(productId, rating);
        return ResponseEntity.ok(ApiResponse.success(response, "Reviews by rating retrieved successfully"));
    }

    @Operation(
            summary = "Get average product rating",
            description = "Calculates and returns the average rating for a product based on all reviews"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Average rating calculated successfully",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Product not found",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/product/{productId}/average-rating")
    public ResponseEntity<ApiResponse<Double>> getAverageRating(
            @Parameter(description = "Product unique identifier", required = true, example = "1")
            @PathVariable Integer productId) {
        double averageRating = reviewService.getAverageRating(productId);
        return ResponseEntity.ok(ApiResponse.success(averageRating, "Average rating calculated successfully"));
    }

    @Operation(
            summary = "Update review",
            description = "Updates an existing review's rating and/or comment"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Review updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReviewResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Review not found",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid review data",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewResponse>> updateReview(
            @Parameter(description = "Review unique identifier", required = true, example = "1")
            @PathVariable Integer id,
            @Valid @RequestBody UpdateReviewRequest request) {
        ReviewResponse response = reviewService.updateReview(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Review updated successfully"));
    }

    @Operation(
            summary = "Mark review as helpful",
            description = "Increments the helpful count for a review when users find it useful"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Review marked as helpful successfully",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Review not found",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PatchMapping("/{id}/helpful")
    public ResponseEntity<ApiResponse<Void>> markReviewAsHelpful(
            @Parameter(description = "Review unique identifier", required = true, example = "1")
            @PathVariable Integer id) {
        reviewService.markReviewAsHelpful(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Review marked as helpful successfully"));
    }

    @Operation(
            summary = "Delete review",
            description = "Permanently deletes a product review"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Review deleted successfully",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Review not found",
                    content = @Content(mediaType = "application/json")
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(
            @Parameter(description = "Review unique identifier", required = true, example = "1")
            @PathVariable Integer id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Review deleted successfully"));
    }
}

package com.shopjoy.graphql.resolver.mutation;

import com.shopjoy.dto.request.CreateReviewRequest;
import com.shopjoy.dto.request.UpdateReviewRequest;
import com.shopjoy.dto.response.ReviewResponse;
import com.shopjoy.graphql.input.CreateReviewInput;
import com.shopjoy.graphql.input.UpdateReviewInput;
import com.shopjoy.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ReviewMutationResolver {

    private final ReviewService reviewService;

    public ReviewMutationResolver(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @MutationMapping
    public ReviewResponse createReview(@Argument @Valid CreateReviewInput input) {
        var request = CreateReviewRequest.builder()
                .productId(input.productId().intValue())
                .userId(input.userId().intValue())
                .rating(input.rating())
                .comment(input.comment())
                .build();
        return reviewService.createReview(request);
    }

    @MutationMapping
    public ReviewResponse updateReview(@Argument Long id, @Argument @Valid UpdateReviewInput input) {
        var request = UpdateReviewRequest.builder()
                .rating(input.rating())
                .comment(input.comment())
                .build();
        return reviewService.updateReview(id.intValue(), request);
    }

    @MutationMapping
    public Boolean deleteReview(@Argument Long id) {
        reviewService.deleteReview(id.intValue());
        return true;
    }
}

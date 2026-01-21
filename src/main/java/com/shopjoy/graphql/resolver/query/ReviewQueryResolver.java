package com.shopjoy.graphql.resolver.query;

import com.shopjoy.dto.response.ReviewResponse;
import com.shopjoy.graphql.type.PageInfo;
import com.shopjoy.graphql.type.ReviewConnection;
import com.shopjoy.service.ReviewService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ReviewQueryResolver {

    private final ReviewService reviewService;

    public ReviewQueryResolver(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @QueryMapping
    public ReviewResponse review(@Argument Long id) {
        return reviewService.getReviewById(id.intValue());
    }

    @QueryMapping
    public ReviewConnection reviews(
            @Argument Long productId,
            @Argument Long userId,
            @Argument Integer page,
            @Argument Integer size
    ) {
        int pageNum = page != null ? page : 0;
        int pageSize = size != null ? size : 20;

        List<ReviewResponse> reviewsList;
        if (productId != null) {
            reviewsList = reviewService.getReviewsByProduct(productId.intValue());
        } else if (userId != null) {
            reviewsList = reviewService.getReviewsByUser(userId.intValue());
        } else {
            reviewsList = List.of();
        }

        int start = pageNum * pageSize;
        int end = Math.min(start + pageSize, reviewsList.size());
        List<ReviewResponse> paginatedReviews = start < reviewsList.size() 
                ? reviewsList.subList(start, end) 
                : List.of();

        PageInfo pageInfo = new PageInfo(
                pageNum,
                pageSize,
                reviewsList.size(),
                (int) Math.ceil((double) reviewsList.size() / pageSize)
        );

        return new ReviewConnection(paginatedReviews, pageInfo);
    }
}

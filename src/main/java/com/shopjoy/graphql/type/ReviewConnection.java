package com.shopjoy.graphql.type;

import com.shopjoy.dto.response.ReviewResponse;

import java.util.List;

public record ReviewConnection(
        List<ReviewResponse> reviews,
        PageInfo pageInfo
) {}

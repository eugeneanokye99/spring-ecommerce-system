package com.shopjoy.graphql.type;

import com.shopjoy.dto.response.OrderResponse;

import java.util.List;

public record OrderConnection(
        List<OrderResponse> orders,
        PageInfo pageInfo
) {}

package com.shopjoy.graphql.type;

import com.shopjoy.dto.response.ProductResponse;

import java.util.List;

public record ProductConnection(
        List<ProductResponse> products,
        PageInfo pageInfo
) {}

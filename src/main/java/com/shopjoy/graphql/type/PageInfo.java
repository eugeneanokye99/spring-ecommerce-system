package com.shopjoy.graphql.type;

public record PageInfo(
        int page,
        int size,
        long totalElements,
        int totalPages
) {}

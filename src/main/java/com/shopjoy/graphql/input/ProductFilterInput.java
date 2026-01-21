package com.shopjoy.graphql.input;

import java.math.BigDecimal;

public record ProductFilterInput(
        BigDecimal minPrice,
        BigDecimal maxPrice,
        Long categoryId,
        String searchTerm,
        Boolean inStock
) {}

package com.shopjoy.graphql.input;

public record UserFilterInput(
        String userType,
        String searchTerm
) {}

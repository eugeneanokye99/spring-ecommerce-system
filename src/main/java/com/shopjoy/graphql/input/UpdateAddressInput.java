package com.shopjoy.graphql.input;

public record UpdateAddressInput(
        String street,
        String city,
        String state,
        String postalCode,
        String country,
        String addressType
) {}

package com.shopjoy.graphql.type;

import com.shopjoy.dto.response.UserResponse;

import java.util.List;

public record UserConnection(
        List<UserResponse> users,
        PageInfo pageInfo
) {}

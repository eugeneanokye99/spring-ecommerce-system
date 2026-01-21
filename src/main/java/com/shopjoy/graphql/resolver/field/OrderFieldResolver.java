package com.shopjoy.graphql.resolver.field;

import com.shopjoy.dto.response.OrderResponse;
import com.shopjoy.dto.response.UserResponse;
import com.shopjoy.service.UserService;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Controller
public class OrderFieldResolver {

    private final UserService userService;

    public OrderFieldResolver(UserService userService) {
        this.userService = userService;
    }

    @SchemaMapping(typeName = "Order", field = "user")
    public UserResponse user(OrderResponse order) {
        return userService.getUserById(order.getUserId());
    }
}

package com.shopjoy.graphql.resolver.field;

import com.shopjoy.dto.response.ProductResponse;
import com.shopjoy.dto.response.ReviewResponse;
import com.shopjoy.dto.response.UserResponse;
import com.shopjoy.service.ProductService;
import com.shopjoy.service.UserService;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ReviewFieldResolver {

    private final ProductService productService;
    private final UserService userService;

    public ReviewFieldResolver(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @SchemaMapping(typeName = "Review", field = "product")
    public ProductResponse product(ReviewResponse review) {
        return productService.getProductById(review.getProductId());
    }

    @SchemaMapping(typeName = "Review", field = "user")
    public UserResponse user(ReviewResponse review) {
        return userService.getUserById(review.getUserId());
    }
}

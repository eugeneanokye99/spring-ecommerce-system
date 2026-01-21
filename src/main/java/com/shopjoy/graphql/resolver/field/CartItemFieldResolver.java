package com.shopjoy.graphql.resolver.field;

import com.shopjoy.dto.response.CartItemResponse;
import com.shopjoy.dto.response.ProductResponse;
import com.shopjoy.service.ProductService;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Controller
public class CartItemFieldResolver {

    private final ProductService productService;

    public CartItemFieldResolver(ProductService productService) {
        this.productService = productService;
    }

    @SchemaMapping(typeName = "CartItem", field = "product")
    public ProductResponse product(CartItemResponse cartItem) {
        return productService.getProductById(cartItem.getProductId());
    }
}

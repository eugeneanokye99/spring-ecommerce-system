package com.shopjoy.graphql.resolver.field;

import com.shopjoy.dto.response.InventoryResponse;
import com.shopjoy.dto.response.ProductResponse;
import com.shopjoy.service.ProductService;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Controller
public class InventoryFieldResolver {

    private final ProductService productService;

    public InventoryFieldResolver(ProductService productService) {
        this.productService = productService;
    }

    @SchemaMapping(typeName = "Inventory", field = "product")
    public ProductResponse product(InventoryResponse inventory) {
        return productService.getProductById(inventory.getProductId());
    }
}

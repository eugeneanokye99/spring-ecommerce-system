package com.shopjoy.graphql.resolver.field;

import com.shopjoy.dto.response.CategoryResponse;
import com.shopjoy.dto.response.ProductResponse;
import com.shopjoy.service.CategoryService;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ProductFieldResolver {

    private final CategoryService categoryService;

    public ProductFieldResolver(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @SchemaMapping(typeName = "Product", field = "category")
    public CategoryResponse category(ProductResponse product) {
        return categoryService.getCategoryById(product.getCategoryId());
    }
}

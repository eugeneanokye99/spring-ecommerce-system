package com.shopjoy.graphql.resolver.field;

import com.shopjoy.dto.response.CategoryResponse;
import com.shopjoy.dto.response.ProductResponse;
import com.shopjoy.service.CategoryService;
import com.shopjoy.service.ProductService;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class CategoryFieldResolver {

    private final CategoryService categoryService;
    private final ProductService productService;

    public CategoryFieldResolver(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @SchemaMapping(typeName = "Category", field = "parentCategory")
    public CategoryResponse parentCategory(CategoryResponse category) {
        if (category.getParentCategoryId() == null) {
            return null;
        }
        return categoryService.getCategoryById(category.getParentCategoryId());
    }

    @SchemaMapping(typeName = "Category", field = "products")
    public List<ProductResponse> products(CategoryResponse category) {
        return productService.getProductsByCategory(category.getCategoryId());
    }
}

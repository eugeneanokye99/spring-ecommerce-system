package com.shopjoy.graphql.resolver.mutation;

import com.shopjoy.dto.response.ProductResponse;
import com.shopjoy.graphql.input.CreateProductInput;
import com.shopjoy.graphql.input.UpdateProductInput;
import com.shopjoy.graphql.mapper.GraphQLMapper;
import com.shopjoy.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ProductMutationResolver {

    private final ProductService productService;
    private final GraphQLMapper graphQLMapper;

    public ProductMutationResolver(ProductService productService, GraphQLMapper graphQLMapper) {
        this.productService = productService;
        this.graphQLMapper = graphQLMapper;
    }

    @MutationMapping
    public ProductResponse createProduct(@Argument @Valid CreateProductInput input) {
        var request = graphQLMapper.toCreateProductRequest(input);
        return productService.createProduct(request);
    }

    @MutationMapping
    public ProductResponse updateProduct(@Argument Long id, @Argument @Valid UpdateProductInput input) {
        var request = graphQLMapper.toUpdateProductRequest(input);
        return productService.updateProduct(id.intValue(), request);
    }

    @MutationMapping
    public Boolean deleteProduct(@Argument Long id) {
        productService.deleteProduct(id.intValue());
        return true;
    }
}

package com.shopjoy.graphql.mapper;

import com.shopjoy.dto.request.*;
import com.shopjoy.graphql.input.*;
import org.springframework.stereotype.Component;

@Component
public class GraphQLMapper {

    public CreateUserRequest toCreateUserRequest(CreateUserInput input) {
        return CreateUserRequest.builder()
                .username(input.username())
                .email(input.email())
                .password(input.password())
                .firstName(input.firstName())
                .lastName(input.lastName())
                .phone(input.phone())
                .build();
    }

    public UpdateUserRequest toUpdateUserRequest(UpdateUserInput input) {
        return UpdateUserRequest.builder()
                .email(input.email())
                .firstName(input.firstName())
                .lastName(input.lastName())
                .phone(input.phone())
                .build();
    }

    public CreateProductRequest toCreateProductRequest(CreateProductInput input) {
        return CreateProductRequest.builder()
                .productName(input.name())
                .description(input.description())
                .price(input.price() != null ? input.price().doubleValue() : null)
                .categoryId(input.categoryId() != null ? input.categoryId().intValue() : null)
                .build();
    }

    public UpdateProductRequest toUpdateProductRequest(UpdateProductInput input) {
        return UpdateProductRequest.builder()
                .productName(input.name())
                .description(input.description())
                .price(input.price() != null ? input.price().doubleValue() : null)
                .build();
    }

    public CreateCategoryRequest toCreateCategoryRequest(CreateCategoryInput input) {
        return CreateCategoryRequest.builder()
                .categoryName(input.name())
                .description(input.description())
                .parentCategoryId(input.parentCategoryId() != null ? input.parentCategoryId().intValue() : null)
                .build();
    }

    public UpdateCategoryRequest toUpdateCategoryRequest(UpdateCategoryInput input) {
        return UpdateCategoryRequest.builder()
                .categoryName(input.name())
                .description(input.description())
                .build();
    }

    public CreateOrderRequest toCreateOrderRequest(CreateOrderInput input) {
        return CreateOrderRequest.builder()
                .userId(input.userId().intValue())
                .build();
    }
}

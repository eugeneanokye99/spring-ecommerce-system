package com.shopjoy.dto.mapper;

import com.shopjoy.dto.request.CreateProductRequest;
import com.shopjoy.dto.request.UpdateProductRequest;
import com.shopjoy.dto.response.ProductResponse;
import com.shopjoy.entity.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper utility for converting between Product entity and DTOs.
 * All methods are static - no need to create an instance.
 * 
 * Usage examples:
 * - Product product = ProductMapper.toProduct(createRequest);
 * - ProductResponse response = ProductMapper.toProductResponse(product);
 * - ProductMapper.updateProductFromRequest(existingProduct, updateRequest);
 */
public class ProductMapper {
    
    /**
     * Converts CreateProductRequest to Product entity.
     * Use this when creating a new product from API request.
     * 
     * @param request the create request from API
     * @return new Product entity (not saved yet)
     */
    public static Product toProduct(CreateProductRequest request) {
        if (request == null) {
            return null;
        }
        
        Product product = new Product();
        product.setProductName(request.getProductName());
        product.setDescription(request.getDescription());
        product.setCategoryId(request.getCategoryId());
        product.setPrice(request.getPrice());
        product.setCostPrice(request.getCostPrice() != null ? request.getCostPrice() : 0.0);
        product.setSku(request.getSku());
        product.setBrand(request.getBrand());
        product.setImageUrl(request.getImageUrl());
        product.setActive(request.getIsActive() != null ? request.getIsActive() : true);
        
        return product;
    }
    
    /**
     * Converts Product entity to ProductResponse.
     * Use this when sending product data back to API clients.
     * 
     * @param product the product entity from database
     * @return response DTO to send to clients
     */
    public static ProductResponse toProductResponse(Product product) {
        if (product == null) {
            return null;
        }
        
        return new ProductResponse(
            product.getProductId(),
            product.getProductName(),
            product.getDescription(),
            product.getCategoryId(),
            product.getPrice(),
            product.getCostPrice(),
            product.getSku(),
            product.getBrand(),
            product.getImageUrl(),
            product.isActive(),
            product.getCreatedAt(),
            product.getUpdatedAt()
        );
    }
    
    /**
     * Converts a list of Product entities to a list of ProductResponse DTOs.
     * Use this for endpoints that return multiple products.
     * 
     * @param products list of product entities
     * @return list of response DTOs
     */
    public static List<ProductResponse> toProductResponseList(List<Product> products) {
        if (products == null) {
            return null;
        }
        
        List<ProductResponse> responses = new ArrayList<>();
        for (Product product : products) {
            responses.add(toProductResponse(product));
        }
        return responses;
    }
    
    /**
     * Updates an existing Product entity with data from UpdateProductRequest.
     * Only updates fields that are not null in the request.
     * This allows partial updates (e.g., only changing price).
     * 
     * @param product the existing product entity to update
     * @param request the update request with new values
     */
    public static void updateProductFromRequest(Product product, UpdateProductRequest request) {
        if (product == null || request == null) {
            return;
        }
        
        // Only update fields that are provided (not null)
        if (request.getProductName() != null) {
            product.setProductName(request.getProductName());
        }
        
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        
        if (request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }
        
        if (request.getCostPrice() != null) {
            product.setCostPrice(request.getCostPrice());
        }
        
        if (request.getBrand() != null) {
            product.setBrand(request.getBrand());
        }
        
        if (request.getImageUrl() != null) {
            product.setImageUrl(request.getImageUrl());
        }
        
        if (request.getIsActive() != null) {
            product.setActive(request.getIsActive());
        }
    }
}

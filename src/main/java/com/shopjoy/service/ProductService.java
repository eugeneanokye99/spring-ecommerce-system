package com.shopjoy.service;

import com.shopjoy.dto.request.CreateProductRequest;
import com.shopjoy.dto.request.UpdateProductRequest;
import com.shopjoy.dto.response.ProductResponse;

import java.util.List;

/**
 * Service interface for Product-related business operations.
 * DTO-FIRST DESIGN: All methods accept and return DTOs, not entities.
 * Service layer handles all DTO ↔ Entity mapping internally.
 */
public interface ProductService {
    
    /**
     * Creates a new product in the catalog.
     * 
     * @param request the product creation request DTO
     * @return the created product response DTO
     */
    ProductResponse createProduct(CreateProductRequest request);
    
    /**
     * Retrieves a product by its ID.
     * 
     * @param productId the product ID
     * @return the product response DTO
     */
    ProductResponse getProductById(Integer productId);
    
    /**
     * Retrieves all products in the catalog.
     * 
     * @return list of all product response DTOs
     */
    List<ProductResponse> getAllProducts();
    
    /**
     * Retrieves all active products (available for sale).
     * 
     * @return list of active product response DTOs
     */
    List<ProductResponse> getActiveProducts();
    
    /**
     * Retrieves all products in a specific category.
     * 
     * @param categoryId the category ID
     * @return list of product response DTOs in the category
     */
    List<ProductResponse> getProductsByCategory(Integer categoryId);
    
    /**
     * Searches for products by name (case-insensitive partial match).
     * 
     * @param keyword the search keyword
     * @return list of matching product response DTOs
     */
    List<ProductResponse> searchProductsByName(String keyword);
    
    /**
     * Finds products within a price range.
     * 
     * @param minPrice minimum price (inclusive)
     * @param maxPrice maximum price (inclusive)
     * @return list of product response DTOs within the price range
     */
    List<ProductResponse> getProductsByPriceRange(double minPrice, double maxPrice);
    
    /**
     * Updates an existing product's information.
     * 
     * @param productId the product ID
     * @param request the update product request DTO
     * @return the updated product response DTO
     */
    ProductResponse updateProduct(Integer productId, UpdateProductRequest request);
    
    /**
     * Updates a product's price.
     * 
     * @param productId the product ID
     * @param newPrice the new price
     * @return the updated product response DTO
     */
    ProductResponse updateProductPrice(Integer productId, double newPrice);
    
    /**
     * Activates a product (makes it available for sale).
     * 
     * @param productId the product ID
     * @return the updated product response DTO
     */
    ProductResponse activateProduct(Integer productId);
    
    /**
     * Deactivates a product (removes from sale, but keeps in catalog).
     * 
     * @param productId the product ID
     * @return the updated product response DTO
     */
    ProductResponse deactivateProduct(Integer productId);
    
    /**
     * Deletes a product from the catalog.
     * 
     * @param productId the product ID
     */
    void deleteProduct(Integer productId);
    
    /**
     * Counts total number of products.
     * 
     * @return total product count
     */
    long getTotalProductCount();
    
    /**
     * Counts products in a specific category.
     * 
     * @param categoryId the category ID
     * @return product count in category
     */
    long getProductCountByCategory(Integer categoryId);
}

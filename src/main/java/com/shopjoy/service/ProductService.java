package com.shopjoy.service;

import com.shopjoy.entity.Product;
import com.shopjoy.exception.DuplicateResourceException;
import com.shopjoy.exception.ResourceNotFoundException;
import com.shopjoy.exception.ValidationException;

import java.util.List;

/**
 * Service interface for Product-related business operations.
 * Handles product CRUD operations, search, and business validations.
 */
public interface ProductService {
    
    /**
     * Creates a new product in the catalog.
     * Validates product data and ensures SKU uniqueness if provided.
     * 
     * @param product the product to create
     * @return the created product with generated ID
     * @throws ValidationException if product data is invalid
     * @throws DuplicateResourceException if SKU already exists
     */
    Product createProduct(Product product);
    
    /**
     * Retrieves a product by its ID.
     * 
     * @param productId the product ID
     * @return the product
     * @throws ResourceNotFoundException if product not found
     */
    Product getProductById(Integer productId);
    
    /**
     * Retrieves all products in the catalog.
     * 
     * @return list of all products
     */
    List<Product> getAllProducts();
    
    /**
     * Retrieves all active products (available for sale).
     * 
     * @return list of active products
     */
    List<Product> getActiveProducts();
    
    /**
     * Retrieves all products in a specific category.
     * 
     * @param categoryId the category ID
     * @return list of products in the category
     */
    List<Product> getProductsByCategory(Integer categoryId);
    
    /**
     * Searches for products by name (case-insensitive partial match).
     * 
     * @param keyword the search keyword
     * @return list of matching products
     */
    List<Product> searchProductsByName(String keyword);
    
    /**
     * Finds products within a price range.
     * 
     * @param minPrice minimum price (inclusive)
     * @param maxPrice maximum price (inclusive)
     * @return list of products within the price range
     * @throws ValidationException if price range is invalid
     */
    List<Product> getProductsByPriceRange(double minPrice, double maxPrice);
    
    /**
     * Updates an existing product's information.
     * Validates product data and price changes.
     * 
     * @param product the product with updated information
     * @return the updated product
     * @throws ResourceNotFoundException if product not found
     * @throws ValidationException if product data is invalid
     */
    Product updateProduct(Product product);
    
    /**
     * Updates a product's price.
     * Validates that the new price is positive.
     * 
     * @param productId the product ID
     * @param newPrice the new price
     * @return the updated product
     * @throws ResourceNotFoundException if product not found
     * @throws ValidationException if price is invalid
     */
    Product updateProductPrice(Integer productId, double newPrice);
    
    /**
     * Activates a product (makes it available for sale).
     * 
     * @param productId the product ID
     * @return the updated product
     * @throws ResourceNotFoundException if product not found
     */
    Product activateProduct(Integer productId);
    
    /**
     * Deactivates a product (removes from sale, but keeps in catalog).
     * 
     * @param productId the product ID
     * @return the updated product
     * @throws ResourceNotFoundException if product not found
     */
    Product deactivateProduct(Integer productId);
    
    /**
     * Deletes a product from the catalog.
     * Should check for dependencies (orders, inventory) before deletion.
     * 
     * @param productId the product ID
     * @throws ResourceNotFoundException if product not found
     * @throws BusinessException if product has dependencies
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

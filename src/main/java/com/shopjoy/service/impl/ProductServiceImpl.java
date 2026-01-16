package com.shopjoy.service.impl;

import com.shopjoy.entity.Product;
import com.shopjoy.exception.ResourceNotFoundException;
import com.shopjoy.exception.ValidationException;
import com.shopjoy.repository.ProductRepository;
import com.shopjoy.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
    
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    
    private final ProductRepository productRepository;
    
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    @Override
    @Transactional(readOnly = false)
    public Product createProduct(Product product) {
        logger.info("Creating new product: {}", product.getProductName());
        
        validateProductData(product);
        
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        product.setActive(true);
        
        Product createdProduct = productRepository.save(product);
        logger.info("Successfully created product with ID: {}", createdProduct.getProductId());
        
        return createdProduct;
    }
    
    @Override
    public Product getProductById(Integer productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
    }
    
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    @Override
    public List<Product> getActiveProducts() {
        return productRepository.findAll().stream()
                .filter(Product::isActive)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Product> getProductsByCategory(Integer categoryId) {
        if (categoryId == null) {
            throw new ValidationException("Category ID cannot be null");
        }
        return productRepository.findByCategoryId(categoryId);
    }
    
    @Override
    public List<Product> searchProductsByName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new ValidationException("Search keyword cannot be empty");
        }
        return productRepository.findByNameContaining(keyword);
    }
    
    @Override
    public List<Product> getProductsByPriceRange(double minPrice, double maxPrice) {
        if (minPrice < 0) {
            throw new ValidationException("Minimum price cannot be negative");
        }
        if (maxPrice < minPrice) {
            throw new ValidationException("Maximum price must be greater than or equal to minimum price");
        }
        return productRepository.findByPriceRange(minPrice, maxPrice);
    }
    
    @Override
    @Transactional(readOnly = false)
    public Product updateProduct(Product product) {
        logger.info("Updating product ID: {}", product.getProductId());
        
        Product existingProduct = getProductById(product.getProductId());
        
        validateProductData(product);
        
        product.setCreatedAt(existingProduct.getCreatedAt());
        product.setUpdatedAt(LocalDateTime.now());
        
        Product updatedProduct = productRepository.update(product);
        logger.info("Successfully updated product ID: {}", product.getProductId());
        
        return updatedProduct;
    }
    
    @Override
    @Transactional(readOnly = false)
    public Product updateProductPrice(Integer productId, double newPrice) {
        logger.info("Updating price for product ID: {} to {}", productId, newPrice);
        
        if (newPrice < 0) {
            throw new ValidationException("price", "must not be negative");
        }
        
        Product product = getProductById(productId);
        product.setPrice(newPrice);
        product.setUpdatedAt(LocalDateTime.now());
        
        Product updatedProduct = productRepository.update(product);
        logger.info("Successfully updated price for product ID: {}", productId);
        
        return updatedProduct;
    }
    
    @Override
    @Transactional(readOnly = false)
    public Product activateProduct(Integer productId) {
        logger.info("Activating product ID: {}", productId);
        
        Product product = getProductById(productId);
        product.setActive(true);
        product.setUpdatedAt(LocalDateTime.now());
        
        return productRepository.update(product);
    }
    
    @Override
    @Transactional(readOnly = false)
    public Product deactivateProduct(Integer productId) {
        logger.info("Deactivating product ID: {}", productId);
        
        Product product = getProductById(productId);
        product.setActive(false);
        product.setUpdatedAt(LocalDateTime.now());
        
        return productRepository.update(product);
    }
    
    @Override
    @Transactional(readOnly = false)
    public void deleteProduct(Integer productId) {
        logger.info("Deleting product ID: {}", productId);
        
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product", "id", productId);
        }
        
        productRepository.delete(productId);
        logger.info("Successfully deleted product ID: {}", productId);
    }
    
    @Override
    public long getTotalProductCount() {
        return productRepository.count();
    }
    
    @Override
    public long getProductCountByCategory(Integer categoryId) {
        if (categoryId == null) {
            throw new ValidationException("Category ID cannot be null");
        }
        return productRepository.countByCategory(categoryId);
    }
    
    private void validateProductData(Product product) {
        if (product == null) {
            throw new ValidationException("Product data cannot be null");
        }
        
        if (product.getProductName() == null || product.getProductName().trim().isEmpty()) {
            throw new ValidationException("productName", "must not be empty");
        }
        
        if (product.getProductName().length() > 200) {
            throw new ValidationException("productName", "must not exceed 200 characters");
        }
        
        if (product.getCategoryId() <= 0) {
            throw new ValidationException("categoryId", "must be a valid category ID");
        }
        
        if (product.getPrice() < 0) {
            throw new ValidationException("price", "must not be negative");
        }
        
        if (product.getCostPrice() < 0) {
            throw new ValidationException("costPrice", "must not be negative");
        }
        
        if (product.getCostPrice() > product.getPrice()) {
            logger.warn("Cost price ({}) is higher than selling price ({}) for product: {}", 
                    product.getCostPrice(), product.getPrice(), product.getProductName());
        }
    }
}

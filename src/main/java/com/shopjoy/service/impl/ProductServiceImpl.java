package com.shopjoy.service.impl;

import com.shopjoy.dto.mapper.ProductMapper;
import com.shopjoy.dto.request.CreateProductRequest;
import com.shopjoy.dto.request.UpdateProductRequest;
import com.shopjoy.dto.response.ProductResponse;
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

/**
 * The type Product service.
 */
@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
    
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    
    private final ProductRepository productRepository;

    /**
     * Instantiates a new Product service.
     *
     * @param productRepository the product repository
     */
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    @Override
    @Transactional()
    public ProductResponse createProduct(CreateProductRequest request) {
        logger.info("Creating new product: {}", request.getProductName());
        
        Product product = ProductMapper.toProduct(request);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        product.setActive(true);
        
        validateProductData(product);
        
        Product createdProduct = productRepository.save(product);
        logger.info("Successfully created product with ID: {}", createdProduct.getProductId());
        
        return ProductMapper.toProductResponse(createdProduct);
    }
    
    @Override
    public ProductResponse getProductById(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        return ProductMapper.toProductResponse(product);
    }
    
    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductMapper::toProductResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ProductResponse> getActiveProducts() {
        return productRepository.findAll().stream()
                .filter(Product::isActive)
                .map(ProductMapper::toProductResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ProductResponse> getProductsByCategory(Integer categoryId) {
        if (categoryId == null) {
            throw new ValidationException("Category ID cannot be null");
        }
        return productRepository.findByCategoryId(categoryId).stream()
                .map(ProductMapper::toProductResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ProductResponse> searchProductsByName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new ValidationException("Search keyword cannot be empty");
        }
        return productRepository.findByNameContaining(keyword).stream()
                .map(ProductMapper::toProductResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ProductResponse> getProductsByPriceRange(double minPrice, double maxPrice) {
        if (minPrice < 0) {
            throw new ValidationException("Minimum price cannot be negative");
        }
        if (maxPrice < minPrice) {
            throw new ValidationException("Maximum price must be greater than or equal to minimum price");
        }
        return productRepository.findByPriceRange(minPrice, maxPrice).stream()
                .map(ProductMapper::toProductResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional()
    public ProductResponse updateProduct(Integer productId, UpdateProductRequest request) {
        logger.info("Updating product ID: {}", productId);
        
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        
        ProductMapper.updateProductFromRequest(existingProduct, request);
        existingProduct.setUpdatedAt(LocalDateTime.now());
        
        validateProductData(existingProduct);
        
        Product updatedProduct = productRepository.update(existingProduct);
        logger.info("Successfully updated product ID: {}", productId);
        
        return ProductMapper.toProductResponse(updatedProduct);
    }
    
    @Override
    @Transactional()
    public ProductResponse updateProductPrice(Integer productId, double newPrice) {
        logger.info("Updating price for product ID: {} to {}", productId, newPrice);
        
        if (newPrice < 0) {
            throw new ValidationException("price", "must not be negative");
        }
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        product.setPrice(newPrice);
        product.setUpdatedAt(LocalDateTime.now());
        
        Product updatedProduct = productRepository.update(product);
        logger.info("Successfully updated price for product ID: {}", productId);
        
        return ProductMapper.toProductResponse(updatedProduct);
    }
    
    @Override
    @Transactional()
    public ProductResponse activateProduct(Integer productId) {
        logger.info("Activating product ID: {}", productId);
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        product.setActive(true);
        product.setUpdatedAt(LocalDateTime.now());
        
        return ProductMapper.toProductResponse(productRepository.update(product));
    }
    
    @Override
    @Transactional()
    public ProductResponse deactivateProduct(Integer productId) {
        logger.info("Deactivating product ID: {}", productId);
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        product.setActive(false);
        product.setUpdatedAt(LocalDateTime.now());
        
        return ProductMapper.toProductResponse(productRepository.update(product));
    }
    
    @Override
    @Transactional()
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

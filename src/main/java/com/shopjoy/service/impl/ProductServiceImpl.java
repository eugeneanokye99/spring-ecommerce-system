package com.shopjoy.service.impl;

import com.shopjoy.dto.filter.ProductFilter;
import com.shopjoy.dto.mapper.ProductMapper;
import com.shopjoy.dto.request.CreateProductRequest;
import com.shopjoy.dto.request.UpdateProductRequest;
import com.shopjoy.dto.response.ProductResponse;
import com.shopjoy.entity.Product;
import com.shopjoy.exception.ResourceNotFoundException;
import com.shopjoy.exception.ValidationException;
import com.shopjoy.repository.ProductRepository;
import com.shopjoy.service.ProductService;
import com.shopjoy.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
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
    
    @Override
    public Page<ProductResponse> getProductsPaginated(Pageable pageable, String sortBy, String sortDirection) {
        logger.info("Fetching products with pagination: page={}, size={}, sortBy={}, sortDirection={}", 
                pageable.getPage(), pageable.getSize(), sortBy, sortDirection);
        
        Page<Product> productPage = productRepository.findAllPaginated(pageable, sortBy, sortDirection);
        
        List<ProductResponse> responseList = productPage.getContent().stream()
                .map(ProductMapper::toProductResponse)
                .collect(Collectors.toList());
        
        return new Page<>(
                responseList,
                productPage.getPageNumber(),
                productPage.getPageSize(),
                productPage.getTotalElements()
        );
    }
    
    @Override
    public Page<ProductResponse> searchProductsPaginated(String keyword, Pageable pageable) {
        logger.info("Searching products with term: '{}', page={}, size={}", 
                keyword, pageable.getPage(), pageable.getSize());
        
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new ValidationException("Search keyword cannot be empty");
        }
        
        Page<Product> productPage = productRepository.searchProductsPaginated(keyword, pageable);
        
        List<ProductResponse> responseList = productPage.getContent().stream()
                .map(ProductMapper::toProductResponse)
                .collect(Collectors.toList());
        
        return new Page<>(
                responseList,
                productPage.getPageNumber(),
                productPage.getPageSize(),
                productPage.getTotalElements()
        );
    }
    
    @Override
    public Page<ProductResponse> getProductsWithFilters(ProductFilter filter, Pageable pageable, String sortBy, String sortDirection) {
        logger.info("Fetching products with filters: minPrice={}, maxPrice={}, categoryId={}, searchTerm={}, page={}, size={}", 
                filter.getMinPrice(), filter.getMaxPrice(), filter.getCategoryId(), 
                filter.getSearchTerm(), pageable.getPage(), pageable.getSize());
        
        if (filter.getMinPrice() != null && filter.getMaxPrice() != null && 
            filter.getMinPrice() > filter.getMaxPrice()) {
            throw new ValidationException("minPrice", "must be less than or equal to maxPrice");
        }
        
        Page<Product> productPage = productRepository.findProductsWithFilters(filter, pageable, sortBy, sortDirection);
        
        List<ProductResponse> responseList = productPage.getContent().stream()
                .map(ProductMapper::toProductResponse)
                .collect(Collectors.toList());
        
        return new Page<>(
                responseList,
                productPage.getPageNumber(),
                productPage.getPageSize(),
                productPage.getTotalElements()
        );
    }
    
    @Override
    public List<ProductResponse> getProductsSortedWithQuickSort(String sortBy, boolean ascending) {
        logger.info("Fetching products sorted with QuickSort: sortBy={}, ascending={}", sortBy, ascending);
        
        List<Product> products = new ArrayList<>(productRepository.findAll());
        
        String direction = ascending ? "ASC" : "DESC";
        Comparator<Product> comparator = ProductComparators.getComparator(sortBy, direction);
        
        SortingAlgorithms.quickSort(products, comparator);
        
        logger.info("Successfully sorted {} products using QuickSort", products.size());
        
        return products.stream()
                .map(ProductMapper::toProductResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ProductResponse> getProductsSortedWithMergeSort(String sortBy, boolean ascending) {
        logger.info("Fetching products sorted with MergeSort: sortBy={}, ascending={}", sortBy, ascending);
        
        List<Product> products = new ArrayList<>(productRepository.findAll());
        
        String direction = ascending ? "ASC" : "DESC";
        Comparator<Product> comparator = ProductComparators.getComparator(sortBy, direction);
        
        SortingAlgorithms.mergeSort(products, comparator);
        
        logger.info("Successfully sorted {} products using MergeSort", products.size());
        
        return products.stream()
                .map(ProductMapper::toProductResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public ProductResponse searchProductByIdWithBinarySearch(Integer productId) {
        logger.info("Searching for product ID: {} using Binary Search", productId);
        
        if (productId == null || productId <= 0) {
            throw new ValidationException("productId", "must be a positive integer");
        }
        
        List<Product> allProducts = new ArrayList<>(productRepository.findAll());
        
        Comparator<Product> comparator = ProductComparators.BY_ID_ASC;
        SortingAlgorithms.quickSort(allProducts, comparator);
        
        Product searchTarget = new Product();
        searchTarget.setProductId(productId);
        
        int index = SearchAlgorithms.binarySearch(allProducts, searchTarget, comparator);
        
        if (index == -1) {
            throw new ResourceNotFoundException("Product", "id", productId);
        }
        
        Product product = allProducts.get(index);
        logger.info("Found product using Binary Search: {}", product.getProductName());
        
        return ProductMapper.toProductResponse(product);
    }
    
    @Override
    public List<ProductResponse> findAllSorted(String sortBy, String sortDirection, String algorithm) {
        logger.info("Fetching products sorted by {} {} using {}", sortBy, sortDirection, algorithm);
        
        List<Product> products = new ArrayList<>(productRepository.findAll());
        Comparator<Product> comparator = ProductComparators.getComparator(sortBy, sortDirection);
        
        switch (algorithm.toUpperCase()) {
            case "QUICKSORT":
                SortingAlgorithms.quickSort(products, comparator);
                break;
            case "MERGESORT":
                SortingAlgorithms.mergeSort(products, comparator);
                break;
            case "HEAPSORT":
                SortingAlgorithms.heapSort(products, comparator);
                break;
            default:
                SortingAlgorithms.quickSort(products, comparator);
        }
        
        return products.stream()
                .map(ProductMapper::toProductResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public Product searchById(Integer id) {
        List<Product> sortedProducts = new ArrayList<>(productRepository.findAll());
        SortingAlgorithms.quickSort(sortedProducts, ProductComparators.BY_ID_ASC);
        
        Product searchTarget = new Product();
        searchTarget.setProductId(id);
        
        int index = SearchAlgorithms.binarySearch(sortedProducts, searchTarget, ProductComparators.BY_ID_ASC);
        
        return index >= 0 ? sortedProducts.get(index) : null;
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

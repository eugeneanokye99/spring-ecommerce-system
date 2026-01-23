package com.shopjoy.service.impl;

import com.shopjoy.dto.mapper.InventoryMapper;
import com.shopjoy.dto.response.InventoryResponse;
import com.shopjoy.entity.Inventory;
import com.shopjoy.exception.DuplicateResourceException;
import com.shopjoy.exception.InsufficientStockException;
import com.shopjoy.exception.ResourceNotFoundException;
import com.shopjoy.exception.ValidationException;
import com.shopjoy.repository.InventoryRepository;
import com.shopjoy.repository.ProductRepository;
import com.shopjoy.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The type Inventory service.
 */
@Service
@Transactional(readOnly = true)
public class InventoryServiceImpl implements InventoryService {

    private static final Logger logger = LoggerFactory.getLogger(InventoryServiceImpl.class);

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    /**
     * Instantiates a new Inventory service.
     *
     * @param inventoryRepository the inventory repository
     * @param productRepository   the product repository
     */
    public InventoryServiceImpl(InventoryRepository inventoryRepository, ProductRepository productRepository) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional()
    public InventoryResponse createInventory(Integer productId, int initialStock, int reorderLevel) {
        logger.info("Creating inventory for product ID: {}", productId);

        Inventory inventory = new Inventory();
        inventory.setProductId(productId);
        inventory.setQuantityInStock(initialStock);
        inventory.setReorderLevel(reorderLevel);

        validateInventoryData(inventory);

        Optional<Inventory> existing = inventoryRepository.findByProductId(inventory.getProductId());
        if (existing.isPresent()) {
            throw new DuplicateResourceException("Inventory", "productId", inventory.getProductId());
        }

        inventory.setLastRestocked(LocalDateTime.now());
        inventory.setUpdatedAt(LocalDateTime.now());

        Inventory createdInventory = inventoryRepository.save(inventory);
        logger.info("Successfully created inventory with ID: {}", createdInventory.getInventoryId());

        return convertToResponse(createdInventory);
    }

    @Override
    public InventoryResponse getInventoryByProduct(Integer productId) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory", "productId", productId));
        return convertToResponse(inventory);
    }

    @Override
    public boolean isProductInStock(Integer productId) {
        try {
            Optional<Inventory> inventory = inventoryRepository.findByProductId(productId);
            return inventory.isPresent() && inventory.get().getQuantityInStock() > 0;
        } catch (ResourceNotFoundException e) {
            return false;
        }
    }

    @Override
    public boolean hasAvailableStock(Integer productId, int quantity) {
        try {
            Optional<Inventory> inventory = inventoryRepository.findByProductId(productId);
            return inventory.isPresent() && inventory.get().getQuantityInStock() >= quantity;
        } catch (ResourceNotFoundException e) {
            return false;
        }
    }

    @Override
    @Transactional()
    public InventoryResponse updateStock(Integer productId, int newQuantity) {
        logger.info("Updating stock for product ID: {} to {}", productId, newQuantity);

        if (newQuantity < 0) {
            throw new ValidationException("quantityInStock", "cannot be negative");
        }

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory", "productId", productId));

        inventoryRepository.updateStock(productId, newQuantity);

        inventory.setQuantityInStock(newQuantity);
        inventory.setUpdatedAt(LocalDateTime.now());

        logger.info("Successfully updated stock for product ID: {}", productId);
        return convertToResponse(inventory);
    }

    @Override
    @Transactional()
    public InventoryResponse addStock(Integer productId, int quantity) {
        logger.info("Adding {} units to product ID: {}", quantity, productId);

        if (quantity <= 0) {
            throw new ValidationException("quantity", "must be positive");
        }

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory", "productId", productId));

        inventoryRepository.incrementStock(productId, quantity);

        inventory.setQuantityInStock(inventory.getQuantityInStock() + quantity);
        inventory.setLastRestocked(LocalDateTime.now());
        inventory.setUpdatedAt(LocalDateTime.now());

        logger.info("Successfully added {} units to product ID: {}", quantity, productId);
        return convertToResponse(inventory);
    }

    @Override
    @Transactional()
    public InventoryResponse removeStock(Integer productId, int quantity) {
        logger.info("Removing {} units from product ID: {}", quantity, productId);

        if (quantity <= 0) {
            throw new ValidationException("quantity", "must be positive");
        }

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory", "productId", productId));

        if (inventory.getQuantityInStock() < quantity) {
            throw new InsufficientStockException(
                    productId,
                    quantity,
                    inventory.getQuantityInStock());
        }

        inventoryRepository.decrementStock(productId, quantity);

        inventory.setQuantityInStock(inventory.getQuantityInStock() - quantity);
        inventory.setUpdatedAt(LocalDateTime.now());

        logger.info("Successfully removed {} units from product ID: {}", quantity, productId);
        return convertToResponse(inventory);
    }

    @Override
    @Transactional()
    public void reserveStock(Integer productId, int quantity) {
        logger.debug("Reserving {} units of product ID: {}", quantity, productId);

        if (quantity <= 0) {
            throw new ValidationException("quantity", "must be positive");
        }

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory", "productId", productId));

        if (inventory.getQuantityInStock() < quantity) {
            throw new InsufficientStockException(
                    productId,
                    quantity,
                    inventory.getQuantityInStock());
        }

        inventoryRepository.decrementStock(productId, quantity);
        logger.debug("Reserved {} units of product ID: {}", quantity, productId);
    }

    @Override
    @Transactional()
    public void releaseStock(Integer productId, int quantity) {
        logger.debug("Releasing {} units of product ID: {}", quantity, productId);

        if (quantity <= 0) {
            throw new ValidationException("quantity", "must be positive");
        }

        inventoryRepository.incrementStock(productId, quantity);
        logger.debug("Released {} units of product ID: {}", quantity, productId);
    }

    @Override
    public List<InventoryResponse> getLowStockProducts() {
        return inventoryRepository.findLowStock().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<InventoryResponse> getOutOfStockProducts() {
        return inventoryRepository.findAll().stream()
                .filter(inventory -> inventory.getQuantityInStock() == 0)
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    @Transactional()
    public InventoryResponse updateReorderLevel(Integer productId, int reorderLevel) {
        logger.info("Updating reorder level for product ID: {} to {}", productId, reorderLevel);

        if (reorderLevel < 0) {
            throw new ValidationException("reorderLevel", "cannot be negative");
        }

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory", "productId", productId));
        inventory.setReorderLevel(reorderLevel);
        inventory.setUpdatedAt(LocalDateTime.now());

        Inventory updatedInventory = inventoryRepository.update(inventory);
        logger.info("Successfully updated reorder level for product ID: {}", productId);

        return convertToResponse(updatedInventory);
    }

    private void validateInventoryData(Inventory inventory) {
        if (inventory == null) {
            throw new ValidationException("Inventory data cannot be null");
        }

        if (inventory.getProductId() <= 0) {
            throw new ValidationException("productId", "must be a valid product ID");
        }

        if (inventory.getQuantityInStock() < 0) {
            throw new ValidationException("quantityInStock", "cannot be negative");
        }

        if (inventory.getReorderLevel() < 0) {
            throw new ValidationException("reorderLevel", "cannot be negative");
        }
    }

    private InventoryResponse convertToResponse(Inventory inventory) {
        String productName = "Unknown Product";
        try {
            productName = productRepository.findById(inventory.getProductId())
                    .map(p -> p.getProductName())
                    .orElse("Unknown Product");
        } catch (Exception e) {
            logger.warn("Could not fetch product name for inventory {}: {}", inventory.getInventoryId(),
                    e.getMessage());
        }
        return InventoryMapper.toInventoryResponse(inventory, productName);
    }
}

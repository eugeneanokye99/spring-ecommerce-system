package com.shopjoy.controller;

import com.shopjoy.dto.mapper.InventoryMapper;
import com.shopjoy.dto.response.ApiResponse;
import com.shopjoy.dto.response.InventoryResponse;
import com.shopjoy.entity.Inventory;
import com.shopjoy.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<InventoryResponse>> getInventoryByProduct(@PathVariable Integer productId) {
        Inventory inventory = inventoryService.getInventoryByProduct(productId);
        InventoryResponse response = InventoryMapper.toInventoryResponse(inventory);
        return ResponseEntity.ok(ApiResponse.success(response, "Inventory retrieved successfully"));
    }

    @GetMapping("/product/{productId}/in-stock")
    public ResponseEntity<ApiResponse<Boolean>> isProductInStock(@PathVariable Integer productId) {
        boolean inStock = inventoryService.isProductInStock(productId);
        return ResponseEntity.ok(ApiResponse.success(inStock, "Stock status checked successfully"));
    }

    @GetMapping("/product/{productId}/available-stock")
    public ResponseEntity<ApiResponse<Boolean>> hasAvailableStock(
            @PathVariable Integer productId,
            @RequestParam Integer quantity) {
        boolean hasStock = inventoryService.hasAvailableStock(productId, quantity);
        return ResponseEntity.ok(ApiResponse.success(hasStock, "Available stock checked successfully"));
    }

    @PutMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<InventoryResponse>> updateStock(
            @PathVariable Integer productId,
            @RequestParam Integer newQuantity) {
        Inventory updatedInventory = inventoryService.updateStock(productId, newQuantity);
        InventoryResponse response = InventoryMapper.toInventoryResponse(updatedInventory);
        return ResponseEntity.ok(ApiResponse.success(response, "Stock updated successfully"));
    }

    @PatchMapping("/product/{productId}/add")
    public ResponseEntity<ApiResponse<InventoryResponse>> addStock(
            @PathVariable Integer productId,
            @RequestParam Integer quantity) {
        Inventory updatedInventory = inventoryService.addStock(productId, quantity);
        InventoryResponse response = InventoryMapper.toInventoryResponse(updatedInventory);
        return ResponseEntity.ok(ApiResponse.success(response, "Stock added successfully"));
    }

    @PatchMapping("/product/{productId}/remove")
    public ResponseEntity<ApiResponse<InventoryResponse>> removeStock(
            @PathVariable Integer productId,
            @RequestParam Integer quantity) {
        Inventory updatedInventory = inventoryService.removeStock(productId, quantity);
        InventoryResponse response = InventoryMapper.toInventoryResponse(updatedInventory);
        return ResponseEntity.ok(ApiResponse.success(response, "Stock removed successfully"));
    }

    @PatchMapping("/product/{productId}/reserve")
    public ResponseEntity<ApiResponse<Void>> reserveStock(
            @PathVariable Integer productId,
            @RequestParam Integer quantity) {
        inventoryService.reserveStock(productId, quantity);
        return ResponseEntity.ok(ApiResponse.success(null, "Stock reserved successfully"));
    }

    @PatchMapping("/product/{productId}/release")
    public ResponseEntity<ApiResponse<Void>> releaseStock(
            @PathVariable Integer productId,
            @RequestParam Integer quantity) {
        inventoryService.releaseStock(productId, quantity);
        return ResponseEntity.ok(ApiResponse.success(null, "Stock released successfully"));
    }

    @GetMapping("/low-stock")
    public ResponseEntity<ApiResponse<List<InventoryResponse>>> getLowStockProducts() {
        List<Inventory> inventories = inventoryService.getLowStockProducts();
        List<InventoryResponse> responses = inventories.stream()
                .map(InventoryMapper::toInventoryResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responses, "Low stock products retrieved successfully"));
    }

    @GetMapping("/out-of-stock")
    public ResponseEntity<ApiResponse<List<InventoryResponse>>> getOutOfStockProducts() {
        List<Inventory> inventories = inventoryService.getOutOfStockProducts();
        List<InventoryResponse> responses = inventories.stream()
                .map(InventoryMapper::toInventoryResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responses, "Out of stock products retrieved successfully"));
    }

    @PatchMapping("/product/{productId}/reorder-level")
    public ResponseEntity<ApiResponse<InventoryResponse>> updateReorderLevel(
            @PathVariable Integer productId,
            @RequestParam Integer reorderLevel) {
        Inventory updatedInventory = inventoryService.updateReorderLevel(productId, reorderLevel);
        InventoryResponse response = InventoryMapper.toInventoryResponse(updatedInventory);
        return ResponseEntity.ok(ApiResponse.success(response, "Reorder level updated successfully"));
    }
}

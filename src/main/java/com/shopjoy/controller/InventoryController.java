package com.shopjoy.controller;

import com.shopjoy.dto.response.ApiResponse;
import com.shopjoy.dto.response.InventoryResponse;
import com.shopjoy.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Inventory management.
 * Base path: /api/v1/inventory
 * THIN CONTROLLER: Only handles HTTP concerns. All business logic and DTO↔Entity mapping done by services.
 */
@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<InventoryResponse>> getInventoryByProduct(@PathVariable Integer productId) {
        InventoryResponse response = inventoryService.getInventoryByProduct(productId);
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
        InventoryResponse response = inventoryService.updateStock(productId, newQuantity);
        return ResponseEntity.ok(ApiResponse.success(response, "Stock updated successfully"));
    }

    @PatchMapping("/product/{productId}/add")
    public ResponseEntity<ApiResponse<InventoryResponse>> addStock(
            @PathVariable Integer productId,
            @RequestParam Integer quantity) {
        InventoryResponse response = inventoryService.addStock(productId, quantity);
        return ResponseEntity.ok(ApiResponse.success(response, "Stock added successfully"));
    }

    @PatchMapping("/product/{productId}/remove")
    public ResponseEntity<ApiResponse<InventoryResponse>> removeStock(
            @PathVariable Integer productId,
            @RequestParam Integer quantity) {
        InventoryResponse response = inventoryService.removeStock(productId, quantity);
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
        List<InventoryResponse> response = inventoryService.getLowStockProducts();
        return ResponseEntity.ok(ApiResponse.success(response, "Low stock products retrieved successfully"));
    }

    @GetMapping("/out-of-stock")
    public ResponseEntity<ApiResponse<List<InventoryResponse>>> getOutOfStockProducts() {
        List<InventoryResponse> response = inventoryService.getOutOfStockProducts();
        return ResponseEntity.ok(ApiResponse.success(response, "Out of stock products retrieved successfully"));
    }

    @PatchMapping("/product/{productId}/reorder-level")
    public ResponseEntity<ApiResponse<InventoryResponse>> updateReorderLevel(
            @PathVariable Integer productId,
            @RequestParam Integer reorderLevel) {
        InventoryResponse response = inventoryService.updateReorderLevel(productId, reorderLevel);
        return ResponseEntity.ok(ApiResponse.success(response, "Reorder level updated successfully"));
    }
}

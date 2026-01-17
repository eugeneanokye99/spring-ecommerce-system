package com.shopjoy.controller;

import com.shopjoy.dto.request.CreateAddressRequest;
import com.shopjoy.dto.request.UpdateAddressRequest;
import com.shopjoy.dto.response.AddressResponse;
import com.shopjoy.dto.response.ApiResponse;
import com.shopjoy.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Address management.
 * Base path: /api/v1/addresses
 * THIN CONTROLLER: Only handles HTTP concerns. All business logic and DTO↔Entity mapping done by services.
 */
@RestController
@RequestMapping("/api/v1/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AddressResponse>> createAddress(
            @Valid @RequestBody CreateAddressRequest request) {
        AddressResponse response = addressService.createAddress(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Address created successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressResponse>> getAddressById(@PathVariable Integer id) {
        AddressResponse response = addressService.getAddressById(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Address retrieved successfully"));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getAddressesByUser(@PathVariable Integer userId) {
        List<AddressResponse> response = addressService.getAddressesByUser(userId);
        return ResponseEntity.ok(ApiResponse.success(response, "User addresses retrieved successfully"));
    }

    @GetMapping("/user/{userId}/default")
    public ResponseEntity<ApiResponse<AddressResponse>> getDefaultAddress(@PathVariable Integer userId) {
        AddressResponse response = addressService.getDefaultAddress(userId);
        return ResponseEntity.ok(ApiResponse.success(response, "Default address retrieved successfully"));
    }

    @PatchMapping("/{id}/set-default")
    public ResponseEntity<ApiResponse<AddressResponse>> setDefaultAddress(@PathVariable Integer id) {
        AddressResponse response = addressService.setDefaultAddress(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Default address set successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressResponse>> updateAddress(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateAddressRequest request) {
        AddressResponse response = addressService.updateAddress(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Address updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(@PathVariable Integer id) {
        addressService.deleteAddress(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Address deleted successfully"));
    }
}

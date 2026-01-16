package com.shopjoy.controller;

import com.shopjoy.dto.mapper.AddressMapper;
import com.shopjoy.dto.request.CreateAddressRequest;
import com.shopjoy.dto.request.UpdateAddressRequest;
import com.shopjoy.dto.response.AddressResponse;
import com.shopjoy.dto.response.ApiResponse;
import com.shopjoy.entity.Address;
import com.shopjoy.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AddressResponse>> createAddress(@Valid @RequestBody CreateAddressRequest request) {
        Address address = AddressMapper.toAddress(request);
        Address createdAddress = addressService.createAddress(address);
        AddressResponse response = AddressMapper.toAddressResponse(createdAddress);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Address created successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressResponse>> getAddressById(@PathVariable Integer id) {
        Address address = addressService.getAddressById(id);
        AddressResponse response = AddressMapper.toAddressResponse(address);
        return ResponseEntity.ok(ApiResponse.success(response, "Address retrieved successfully"));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getAddressesByUser(@PathVariable Integer userId) {
        List<Address> addresses = addressService.getAddressesByUser(userId);
        List<AddressResponse> responses = addresses.stream()
                .map(AddressMapper::toAddressResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responses, "User addresses retrieved successfully"));
    }

    @GetMapping("/user/{userId}/default")
    public ResponseEntity<ApiResponse<AddressResponse>> getDefaultAddress(@PathVariable Integer userId) {
        Address address = addressService.getDefaultAddress(userId);
        if (address == null) {
            return ResponseEntity.ok(ApiResponse.success(null, "No default address found"));
        }
        AddressResponse response = AddressMapper.toAddressResponse(address);
        return ResponseEntity.ok(ApiResponse.success(response, "Default address retrieved successfully"));
    }

    @PatchMapping("/{id}/set-default")
    public ResponseEntity<ApiResponse<AddressResponse>> setDefaultAddress(
            @PathVariable Integer id) {
        Address updatedAddress = addressService.setDefaultAddress(id);
        AddressResponse response = AddressMapper.toAddressResponse(updatedAddress);
        return ResponseEntity.ok(ApiResponse.success(response, "Default address set successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressResponse>> updateAddress(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateAddressRequest request) {
        Address address = addressService.getAddressById(id);
        AddressMapper.updateAddressFromRequest(address, request);
        Address updatedAddress = addressService.updateAddress(address);
        AddressResponse response = AddressMapper.toAddressResponse(updatedAddress);
        return ResponseEntity.ok(ApiResponse.success(response, "Address updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(@PathVariable Integer id) {
        addressService.deleteAddress(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Address deleted successfully"));
    }
}

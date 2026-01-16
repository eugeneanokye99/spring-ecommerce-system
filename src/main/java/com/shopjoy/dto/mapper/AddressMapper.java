package com.shopjoy.dto.mapper;

import com.shopjoy.dto.request.CreateAddressRequest;
import com.shopjoy.dto.request.UpdateAddressRequest;
import com.shopjoy.dto.response.AddressResponse;
import com.shopjoy.entity.Address;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper for Address entity and DTOs.
 */
public class AddressMapper {
    
    public static Address toAddress(CreateAddressRequest request) {
        if (request == null) {
            return null;
        }
        
        Address address = new Address();
        address.setUserId(request.getUserId());
        address.setAddressType(request.getAddressType());
        address.setStreetAddress(request.getStreetAddress());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPostalCode(request.getPostalCode());
        address.setCountry(request.getCountry());
        address.setDefault(request.getIsDefault() != null ? request.getIsDefault() : false);
        
        return address;
    }
    
    public static AddressResponse toAddressResponse(Address address) {
        if (address == null) {
            return null;
        }
        
        return new AddressResponse(
            address.getAddressId(),
            address.getUserId(),
            address.getAddressType(),
            address.getStreetAddress(),
            address.getCity(),
            address.getState(),
            address.getPostalCode(),
            address.getCountry(),
            address.isDefault(),
            address.getCreatedAt()
        );
    }
    
    public static List<AddressResponse> toAddressResponseList(List<Address> addresses) {
        if (addresses == null) {
            return null;
        }
        
        List<AddressResponse> responses = new ArrayList<>();
        for (Address address : addresses) {
            responses.add(toAddressResponse(address));
        }
        return responses;
    }
    
    public static void updateAddressFromRequest(Address address, UpdateAddressRequest request) {
        if (address == null || request == null) {
            return;
        }
        
        if (request.getAddressType() != null) {
            address.setAddressType(request.getAddressType());
        }
        
        if (request.getStreetAddress() != null) {
            address.setStreetAddress(request.getStreetAddress());
        }
        
        if (request.getCity() != null) {
            address.setCity(request.getCity());
        }
        
        if (request.getState() != null) {
            address.setState(request.getState());
        }
        
        if (request.getPostalCode() != null) {
            address.setPostalCode(request.getPostalCode());
        }
        
        if (request.getCountry() != null) {
            address.setCountry(request.getCountry());
        }
        
        if (request.getIsDefault() != null) {
            address.setDefault(request.getIsDefault());
        }
    }
}

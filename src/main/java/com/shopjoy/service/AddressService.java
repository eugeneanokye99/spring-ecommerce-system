package com.shopjoy.service;

import com.shopjoy.dto.request.CreateAddressRequest;
import com.shopjoy.dto.request.UpdateAddressRequest;
import com.shopjoy.dto.response.AddressResponse;
import com.shopjoy.exception.ResourceNotFoundException;
import com.shopjoy.exception.ValidationException;

import java.util.List;

/**
 * Service interface for Address management operations.
 * Handles user addresses and default address management.
 */
public interface AddressService {
    
    /**
     * Creates a new address for a user.
     * If marked as default, unsets other default addresses.
     * 
     * @param request the create address request
     * @return the created address response
     * @throws ResourceNotFoundException if user not found
     * @throws ValidationException if address data is invalid
     */
    AddressResponse createAddress(CreateAddressRequest request);
    
    /**
     * Retrieves an address by its ID.
     * 
     * @param addressId the address ID
     * @return the address response
     * @throws ResourceNotFoundException if address not found
     */
    AddressResponse getAddressById(Integer addressId);
    
    /**
     * Retrieves all addresses for a specific user.
     * 
     * @param userId the user ID
     * @return list of address responses
     */
    List<AddressResponse> getAddressesByUser(Integer userId);
    
    /**
     * Retrieves the default address for a user.
     * 
     * @param userId the user ID
     * @return the default address response
     * @throws ResourceNotFoundException if no default address found
     */
    AddressResponse getDefaultAddress(Integer userId);
    
    /**
     * Sets an address as the default for the user.
     * Unsets the previous default address.
     * 
     * @param addressId the address ID
     * @return the updated address response
     * @throws ResourceNotFoundException if address not found
     */
    AddressResponse setDefaultAddress(Integer addressId);
    
    /**
     * Updates an existing address.
     * 
     * @param addressId the address ID
     * @param request the update address request
     * @return the updated address response
     * @throws ResourceNotFoundException if address not found
     * @throws ValidationException if address data is invalid
     */
    AddressResponse updateAddress(Integer addressId, UpdateAddressRequest request);
    
    /**
     * Deletes an address.
     * If deleting default address, another address should be set as default.
     * 
     * @param addressId the address ID
     * @throws ResourceNotFoundException if address not found
     */
    void deleteAddress(Integer addressId);
}

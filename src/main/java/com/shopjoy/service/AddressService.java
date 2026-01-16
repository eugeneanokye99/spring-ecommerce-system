package com.shopjoy.service;

import com.shopjoy.entity.Address;
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
     * @param address the address to create
     * @return the created address with generated ID
     * @throws ResourceNotFoundException if user not found
     * @throws ValidationException if address data is invalid
     */
    Address createAddress(Address address);
    
    /**
     * Retrieves an address by its ID.
     * 
     * @param addressId the address ID
     * @return the address
     * @throws ResourceNotFoundException if address not found
     */
    Address getAddressById(Integer addressId);
    
    /**
     * Retrieves all addresses for a specific user.
     * 
     * @param userId the user ID
     * @return list of addresses
     */
    List<Address> getAddressesByUser(Integer userId);
    
    /**
     * Retrieves the default address for a user.
     * 
     * @param userId the user ID
     * @return the default address
     * @throws ResourceNotFoundException if no default address found
     */
    Address getDefaultAddress(Integer userId);
    
    /**
     * Sets an address as the default for the user.
     * Unsets the previous default address.
     * 
     * @param addressId the address ID
     * @return the updated address
     * @throws ResourceNotFoundException if address not found
     */
    Address setDefaultAddress(Integer addressId);
    
    /**
     * Updates an existing address.
     * 
     * @param address the address with updated information
     * @return the updated address
     * @throws ResourceNotFoundException if address not found
     * @throws ValidationException if address data is invalid
     */
    Address updateAddress(Address address);
    
    /**
     * Deletes an address.
     * If deleting default address, another address should be set as default.
     * 
     * @param addressId the address ID
     * @throws ResourceNotFoundException if address not found
     */
    void deleteAddress(Integer addressId);
}

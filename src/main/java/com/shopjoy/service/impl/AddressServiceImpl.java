package com.shopjoy.service.impl;

import com.shopjoy.entity.Address;
import com.shopjoy.exception.ResourceNotFoundException;
import com.shopjoy.exception.ValidationException;
import com.shopjoy.repository.AddressRepository;
import com.shopjoy.service.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class AddressServiceImpl implements AddressService {
    
    private static final Logger logger = LoggerFactory.getLogger(AddressServiceImpl.class);
    
    private final AddressRepository addressRepository;
    
    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }
    
    @Override
    @Transactional(readOnly = false)
    public Address createAddress(Address address) {
        logger.info("Creating new address for user {}", address.getUserId());
        
        validateAddressData(address);
        address.setCreatedAt(LocalDateTime.now());
        
        return addressRepository.save(address);
    }
    
    @Override
    public Address getAddressById(Integer addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));
    }
    
    @Override
    public List<Address> getAddressesByUser(Integer userId) {
        logger.info("Fetching addresses for user {}", userId);
        return addressRepository.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = false)
    public Address updateAddress(Address address) {
        logger.info("Updating address ID: {}", address.getAddressId());
        
        getAddressById(address.getAddressId());
        validateAddressData(address);
        
        return addressRepository.update(address);
    }
    
    @Override
    @Transactional(readOnly = false)
    public void deleteAddress(Integer addressId) {
        logger.info("Deleting address ID: {}", addressId);
        
        if (!addressRepository.existsById(addressId)) {
            throw new ResourceNotFoundException("Address", "id", addressId);
        }
        
        addressRepository.delete(addressId);
    }
    
    @Override
    @Transactional(readOnly = false)
    public Address setDefaultAddress(Integer addressId) {
        logger.info("Setting default address {}", addressId);
        
        Address address = getAddressById(addressId);
        int userId = address.getUserId();
        
        addressRepository.clearDefaultAddresses(userId);
        return addressRepository.setDefaultAddress(addressId);
    }
    
    @Override
    public Address getDefaultAddress(Integer userId) {
        return addressRepository.findDefaultAddress(userId)
                .orElse(null);
    }
    
    private void validateAddressData(Address address) {
        if (address == null) {
            throw new ValidationException("Address data cannot be null");
        }
        
        if (address.getUserId() == 0) {
            throw new ValidationException("userId", "is required");
        }
        
        if (address.getStreetAddress() == null || address.getStreetAddress().trim().isEmpty()) {
            throw new ValidationException("streetAddress", "must not be empty");
        }
        
        if (address.getCity() == null || address.getCity().trim().isEmpty()) {
            throw new ValidationException("city", "must not be empty");
        }
        
        if (address.getState() == null || address.getState().trim().isEmpty()) {
            throw new ValidationException("state", "must not be empty");
        }
        
        if (address.getPostalCode() == null || address.getPostalCode().trim().isEmpty()) {
            throw new ValidationException("postalCode", "must not be empty");
        }
        
        if (address.getCountry() == null || address.getCountry().trim().isEmpty()) {
            throw new ValidationException("country", "must not be empty");
        }
    }
}

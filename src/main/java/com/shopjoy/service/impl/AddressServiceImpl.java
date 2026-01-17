package com.shopjoy.service.impl;

import com.shopjoy.dto.mapper.AddressMapper;
import com.shopjoy.dto.request.CreateAddressRequest;
import com.shopjoy.dto.request.UpdateAddressRequest;
import com.shopjoy.dto.response.AddressResponse;
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
import java.util.stream.Collectors;

/**
 * The type Address service.
 */
@Service
@Transactional(readOnly = true)
public class AddressServiceImpl implements AddressService {
    
    private static final Logger logger = LoggerFactory.getLogger(AddressServiceImpl.class);
    
    private final AddressRepository addressRepository;

    /**
     * Instantiates a new Address service.
     *
     * @param addressRepository the address repository
     */
    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }
    
    @Override
    @Transactional()
    public AddressResponse createAddress(CreateAddressRequest request) {
        logger.info("Creating new address for user {}", request.getUserId());
        
        Address address = AddressMapper.toAddress(request);
        address.setCreatedAt(LocalDateTime.now());
        
        Address savedAddress = addressRepository.save(address);
        return AddressMapper.toAddressResponse(savedAddress);
    }
    
    @Override
    public AddressResponse getAddressById(Integer addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));
        return AddressMapper.toAddressResponse(address);
    }
    
    @Override
    public List<AddressResponse> getAddressesByUser(Integer userId) {
        logger.info("Fetching addresses for user {}", userId);
        List<Address> addresses = addressRepository.findByUserId(userId);
        return addresses.stream()
                .map(AddressMapper::toAddressResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional()
    public AddressResponse updateAddress(Integer addressId, UpdateAddressRequest request) {
        logger.info("Updating address ID: {}", addressId);
        
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));
        
        AddressMapper.updateAddressFromRequest(address, request);
        
        Address updatedAddress = addressRepository.update(address);
        return AddressMapper.toAddressResponse(updatedAddress);
    }
    
    @Override
    @Transactional()
    public void deleteAddress(Integer addressId) {
        logger.info("Deleting address ID: {}", addressId);
        
        if (!addressRepository.existsById(addressId)) {
            throw new ResourceNotFoundException("Address", "id", addressId);
        }
        
        addressRepository.delete(addressId);
    }
    
    @Override
    @Transactional()
    public AddressResponse setDefaultAddress(Integer addressId) {
        logger.info("Setting default address {}", addressId);
        
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));
        int userId = address.getUserId();
        
        addressRepository.clearDefaultAddresses(userId);
        Address updatedAddress = addressRepository.setDefaultAddress(addressId);
        return AddressMapper.toAddressResponse(updatedAddress);
    }
    
    @Override
    public AddressResponse getDefaultAddress(Integer userId) {
        Address address = addressRepository.findDefaultAddress(userId)
                .orElse(null);
        return address != null ? AddressMapper.toAddressResponse(address) : null;
    }
}

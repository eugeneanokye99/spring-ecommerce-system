package com.shopjoy.dto.response;

import com.shopjoy.entity.AddressType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Response DTO for Address.
 */
@Setter
@Getter
public class AddressResponse {
    
    private int addressId;
    private int userId;
    private AddressType addressType;
    private String streetAddress;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private boolean isDefault;
    private LocalDateTime createdAt;

}

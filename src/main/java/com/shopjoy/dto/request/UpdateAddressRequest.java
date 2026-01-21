package com.shopjoy.dto.request;

import com.shopjoy.entity.AddressType;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Request DTO for updating an existing address.
 */
@Setter
@Getter
@Builder
public class UpdateAddressRequest {
    
    private AddressType addressType;
    
    @Size(min = 5, max = 200, message = "Street address must be between 5 and 200 characters")
    private String streetAddress;
    
    @Size(max = 100, message = "City cannot exceed 100 characters")
    private String city;
    
    @Size(max = 100, message = "State cannot exceed 100 characters")
    private String state;
    
    @Size(max = 20, message = "Postal code cannot exceed 20 characters")
    private String postalCode;
    
    @Size(max = 100, message = "Country cannot exceed 100 characters")
    private String country;
    
    private Boolean isDefault;


}

package com.shopjoy.graphql.resolver.mutation;

import com.shopjoy.dto.request.CreateAddressRequest;
import com.shopjoy.dto.request.UpdateAddressRequest;
import com.shopjoy.dto.response.AddressResponse;
import com.shopjoy.entity.AddressType;
import com.shopjoy.graphql.input.CreateAddressInput;
import com.shopjoy.graphql.input.UpdateAddressInput;
import com.shopjoy.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class AddressMutationResolver {

    private final AddressService addressService;

    public AddressMutationResolver(AddressService addressService) {
        this.addressService = addressService;
    }

    @MutationMapping
    public AddressResponse createAddress(@Argument @Valid CreateAddressInput input) {
        var request = CreateAddressRequest.builder()
                .userId(input.userId().intValue())
                .addressType(AddressType.valueOf(input.addressType()))
                .streetAddress(input.street())
                .city(input.city())
                .state(input.state())
                .postalCode(input.postalCode())
                .country(input.country())
                .isDefault(input.isDefault() != null ? input.isDefault() : false)
                .build();
        return addressService.createAddress(request);
    }

    @MutationMapping
    public AddressResponse updateAddress(@Argument Long id, @Argument @Valid UpdateAddressInput input) {
        var request = UpdateAddressRequest.builder()
                .streetAddress(input.street())
                .city(input.city())
                .state(input.state())
                .postalCode(input.postalCode())
                .country(input.country())
                .addressType(input.addressType() != null ? AddressType.valueOf(input.addressType()) : null)
                .build();
        return addressService.updateAddress(id.intValue(), request);
    }

    @MutationMapping
    public Boolean deleteAddress(@Argument Long id) {
        addressService.deleteAddress(id.intValue());
        return true;
    }

    @MutationMapping
    public AddressResponse setDefaultAddress(@Argument Long id) {
        return addressService.setDefaultAddress(id.intValue());
    }
}

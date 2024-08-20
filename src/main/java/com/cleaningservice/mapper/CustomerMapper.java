package com.cleaningservice.mapper;

import com.cleaningservice.model.entity.Customer;
import com.cleaningservice.model.request.CreateCustomerRequest;
import com.cleaningservice.model.dto.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper extends TwoWayMapper<Customer, CustomerDTO> {
    CustomerDTO mapRequestToDto(CreateCustomerRequest createCustomerRequest);
}

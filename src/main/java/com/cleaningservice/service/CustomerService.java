package com.cleaningservice.service;

import com.cleaningservice.model.request.CreateCustomerRequest;
import com.cleaningservice.model.dto.CustomerDTO;

public interface CustomerService {
    CustomerDTO createCustomer(CreateCustomerRequest createCustomerRequest);

    CustomerDTO getCustomer(Long customerId);
}

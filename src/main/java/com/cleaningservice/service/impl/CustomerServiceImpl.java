package com.cleaningservice.service.impl;

import com.cleaningservice.model.entity.Customer;
import com.cleaningservice.exception.EntityNotFoundException;
import com.cleaningservice.mapper.CustomerMapper;
import com.cleaningservice.model.request.CreateCustomerRequest;
import com.cleaningservice.model.dto.CustomerDTO;
import com.cleaningservice.repository.CustomerRepository;
import com.cleaningservice.service.CustomerService;
import com.cleaningservice.util.UriUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerDTO createCustomer(final CreateCustomerRequest createCustomerRequest) {
        final CustomerDTO customerDTO = customerMapper.mapRequestToDto(createCustomerRequest);
        final Customer customer = customerMapper.toEntity(customerDTO);
        return customerMapper.toDto(customerRepository.save(customer));
    }

    @Override
    public CustomerDTO getCustomer(final Long id) {
        final Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Customer with id: %s is not found", id)));
        final CustomerDTO customerDTO = customerMapper.toDto(customer);
        final List<Link> links = UriUtil.addLinks(id, CustomerDTO.class);
        customerDTO.add(links);
        return customerDTO;
    }
}

package com.cleaningservice.controller;

import com.cleaningservice.model.request.CreateCustomerRequest;
import com.cleaningservice.model.dto.CustomerDTO;
import com.cleaningservice.service.CustomerService;
import com.cleaningservice.util.UriUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
@Slf4j
public class CustomerController {
    private final CustomerService customerService;

    /**
     * GET : get a specific customer by ID
     *
     * @param id customer ID
     * @return http status ok (200) and the required customer details in the body
     */
    @Operation(summary = "Get customer by ID")
    @GetMapping("/{id}")
    ResponseEntity<CustomerDTO> getCustomer(@PathVariable Long id) {
        log.info("Rest request to get a customer by id: {}", id);
        return ResponseEntity.ok(customerService.getCustomer(id));
    }

    /**
     * POST : create new customer
     *
     * @return the ResponseEntity with http status created (201) and the created customer in the body
     */
    @Operation(summary = "Create customer")
    @PostMapping
    ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CreateCustomerRequest createCustomerRequest) {
        log.info("Rest request to create a new customer");
        CustomerDTO response = customerService.createCustomer(createCustomerRequest);
        return ResponseEntity.created(UriUtil.locationUri(response.getId().toString())).body(response);
    }
}

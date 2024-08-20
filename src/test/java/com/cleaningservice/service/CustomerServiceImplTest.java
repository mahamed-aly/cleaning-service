package com.cleaningservice.service;

import com.cleaningservice.exception.EntityNotFoundException;
import com.cleaningservice.mapper.CustomerMapper;
import com.cleaningservice.model.dto.CustomerDTO;
import com.cleaningservice.repository.CustomerRepository;
import com.cleaningservice.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.cleaningservice.util.TestUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CustomerMapper customerMapper;
    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    void testGetCustomer_success() {
        // Arrange
        doReturn(Optional.of(CUSTOMER)).when(customerRepository).findById(CUSTOMER_ID);
        doReturn(CUSTOMER_DTO).when(customerMapper).toDto(CUSTOMER);

        // Act
        CustomerDTO actual = customerService.getCustomer(CUSTOMER_ID);

        // Assert
        verify(customerRepository).findById(CUSTOMER_ID);
        verify(customerMapper).toDto(CUSTOMER);
        assertEquals(CUSTOMER_DTO, actual);

    }

    @Test
    void testGetCustomer_throwsEntityNotFoundException() {
        // Arrange
        doReturn(Optional.empty()).when(customerRepository).findById(CUSTOMER_ID);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                customerService.getCustomer(CUSTOMER_ID)
        );
        assertEquals("Customer with id: 1 is not found", exception.getMessage());
    }

    @Test
    void testCreateCustomer() {
        // Arrange
        doReturn(CUSTOMER_DTO).when(customerMapper).mapRequestToDto(CREATE_CUSTOMER_REQUEST);
        doReturn(CUSTOMER).when(customerMapper).toEntity(CUSTOMER_DTO);
        doReturn(CUSTOMER_DTO).when(customerMapper).toDto(CUSTOMER);
        doReturn(CUSTOMER).when(customerRepository).save(CUSTOMER);

        // Act
        CustomerDTO actual = customerService.createCustomer(CREATE_CUSTOMER_REQUEST);

        // Assert
        verify(customerMapper).mapRequestToDto(CREATE_CUSTOMER_REQUEST);
        verify(customerMapper).toEntity(CUSTOMER_DTO);
        verify(customerRepository).save(CUSTOMER);
        verify(customerMapper).toDto(CUSTOMER);
        assertEquals(CUSTOMER_DTO, actual);
    }
}

package com.TOM.tom_mini.crm.controller;

import com.TOM.tom_mini.crm.entity.Customer;
import com.TOM.tom_mini.crm.request.CustomerRegistrationRequest;
import com.TOM.tom_mini.crm.response.CustomerInfoResponse;
import com.TOM.tom_mini.crm.service.CustomerService;
import com.TOM.tom_mini.crm.mapper.CustomerMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerCustomer_ShouldReturnCreatedStatus_WhenSuccessful() {
        // Given
        CustomerRegistrationRequest request = new CustomerRegistrationRequest();
        Customer customer = new Customer();
        CustomerInfoResponse response = new CustomerInfoResponse();

        when(customerService.registerCustomer(request)).thenReturn(customer);
        when(customerMapper.customerToCustomerInfoResponse(customer)).thenReturn(response);

        // When
        ResponseEntity<CustomerInfoResponse> result = customerController.registerCustomer(request);

        // Then
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void getCustomerById_ShouldReturnCustomerInfo_WhenCustomerExists() {
        // Given
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setId(customerId);
        CustomerInfoResponse response = new CustomerInfoResponse();

        when(customerService.getCustomerById(customerId)).thenReturn(Optional.of(customer));
        when(customerMapper.mapToCustomerInfoResponse(customer)).thenReturn(response);

        // When
        ResponseEntity<CustomerInfoResponse> result = customerController.getCustomerById(customerId);

        // Then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void getCustomerById_ShouldReturnNotFound_WhenCustomerDoesNotExist() {
        // Given
        Long customerId = 1L;
        when(customerService.getCustomerById(customerId)).thenReturn(Optional.empty());

        // When
        ResponseEntity<CustomerInfoResponse> result = customerController.getCustomerById(customerId);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void sayHello_ShouldReturnHelloWorld() {
        // When
        String result = customerController.sayHello();

        // Then
        assertEquals("Hello World", result);
    }
}

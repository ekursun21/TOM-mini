package com.TOM.tom_mini.crm.service;

import com.TOM.tom_mini.crm.entity.Customer;
import com.TOM.tom_mini.crm.exception.CustomerAlreadyExistsException;
import com.TOM.tom_mini.crm.repository.CustomerRepository;
import com.TOM.tom_mini.crm.repository.AddressRepository;
import com.TOM.tom_mini.crm.request.CustomerRegistrationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private CustomerAddressRepository customerAddressRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerCustomer_ShouldThrowException_WhenCustomerAlreadyExists() {
        // Given
        CustomerRegistrationRequest request = new CustomerRegistrationRequest();
        request.setEmail("existing@example.com");
        when(customerRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(new Customer()));

        // When & Then
        assertThrows(CustomerAlreadyExistsException.class, () -> customerService.registerCustomer(request));
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void registerCustomer_ShouldRegisterCustomerSuccessfully_WhenCustomerDoesNotExist() {
        // Given
        CustomerRegistrationRequest request = new CustomerRegistrationRequest();
        request.setEmail("new@example.com");
        request.setName("John");
        request.setSurname("Doe");
        request.setPassword("password");
        request.setBirthday(LocalDate.of(1990, 1, 1));
        request.setAddresses(new ArrayList<>()); // Assuming no addresses for simplicity

        when(customerRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        // When
        Customer registeredCustomer = customerService.registerCustomer(request);

        // Then
        assertNotNull(registeredCustomer);
        assertEquals("John", registeredCustomer.getName());
        assertEquals("Doe", registeredCustomer.getSurname());
        assertEquals("encodedPassword", registeredCustomer.getPassword());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void getCustomerById_ShouldReturnCustomer_WhenCustomerExists() {
        // Given
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setId(customerId);
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        // When
        Optional<Customer> result = customerService.getCustomerById(customerId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(customerId, result.get().getId());
    }

    @Test
    void getCustomerById_ShouldReturnEmpty_WhenCustomerDoesNotExist() {
        // Given
        Long customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // When
        Optional<Customer> result = customerService.getCustomerById(customerId);

        // Then
        assertFalse(result.isPresent());
    }
}

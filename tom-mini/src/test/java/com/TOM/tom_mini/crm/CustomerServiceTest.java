package com.TOM.tom_mini.crm;

import com.TOM.tom_mini.crm.other.IdGenerator;
import com.TOM.tom_mini.crm.request.CustomerRegistrationRequest;
import com.TOM.tom_mini.crm.entity.Customer;
import com.TOM.tom_mini.crm.other.Role;
import com.TOM.tom_mini.crm.repository.AddressRepository;
import com.TOM.tom_mini.crm.repository.CustomerRepository;
import com.TOM.tom_mini.crm.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    void registerCustomer_shouldEncodePasswordAndSaveCustomer() {
        // Given
        CustomerRegistrationRequest request = new CustomerRegistrationRequest();
        request.setName("John");
        request.setSurname("Doe");
        request.setPassword("plainPassword");
        request.setPhoneNumber("123456789");
        request.setBirthday(LocalDate.of(1990, 1, 1));
        request.setEmail("john.doe@example.com");
        request.setAddresses(Collections.emptyList());

        Customer savedCustomer = Customer.builder()
                .id(IdGenerator.generate())
                .name(request.getName())
                .surname(request.getSurname())
                .password("encodedPassword")
                .phoneNumber(request.getPhoneNumber())
                .birthday(request.getBirthday())
                .email(request.getEmail())
                .role(Role.USER)
                .createdAt(LocalDate.now())
                .modifiedAt(LocalDate.now())
                .build();

        // Mock the behavior of passwordEncoder and customerRepository
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        // When
        Customer result = customerService.registerCustomer(request);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).save(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertEquals("encodedPassword", capturedCustomer.getPassword());
        assertEquals("John", capturedCustomer.getName());
        assertEquals("Doe", capturedCustomer.getSurname());
        assertEquals("john.doe@example.com", capturedCustomer.getEmail());
        assertEquals(Role.USER, capturedCustomer.getRole());
        assertNotNull(result.getId());
    }
}

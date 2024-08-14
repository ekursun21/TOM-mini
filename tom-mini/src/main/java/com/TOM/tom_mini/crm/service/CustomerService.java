package com.TOM.tom_mini.crm.service;

import com.TOM.tom_mini.crm.request.CustomerRegistrationRequest;
import com.TOM.tom_mini.crm.entity.*;
import com.TOM.tom_mini.crm.repository.AddressRepository;
import com.TOM.tom_mini.crm.repository.CustomerAddressRepository;
import com.TOM.tom_mini.crm.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final CustomerAddressRepository customerAddressRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, AddressRepository addressRepository,
                           CustomerAddressRepository addressAddressRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
        this.customerAddressRepository = addressAddressRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Customer registerCustomer(CustomerRegistrationRequest request) {
        log.info("Registering customer with email: {}", request.getEmail());

        Customer customer = Customer.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .birthday(request.getBirthday())
                .email(request.getEmail())
                .role(Role.valueOf("USER"))  // Use a singleton set to initialize roles
                .createdAt(LocalDate.now())
                .modifiedAt(LocalDate.now())
                .build();

        customer = customerRepository.save(customer);
        Set<CustomerAddress> customerAddresses = new HashSet<>();

        for (Address address : request.getAddresses()) {
            addressRepository.save(address);

            CustomerAddress customerAddress = new CustomerAddress(new CustomerAddressId(
                    customer.getId(),
                    address.getId()),
                    customer,
                    address);
            customerAddressRepository.save(customerAddress);

            //customerAddresses.add(customerAddress);
        }
        customer.setCustomerAddresses(customerAddresses);
        log.debug("Customer registration details: {}", request);
        return customer;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(UUID id) {
        return customerRepository.findById(id);
    }

    public Optional<Customer> getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }
}

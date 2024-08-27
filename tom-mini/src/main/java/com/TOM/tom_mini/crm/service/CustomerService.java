package com.TOM.tom_mini.crm.service;

import com.TOM.tom_mini.crm.exception.CustomerAlreadyExistsException;
import com.TOM.tom_mini.crm.other.IdGenerator;
import com.TOM.tom_mini.crm.other.Role;
import com.TOM.tom_mini.crm.request.CustomerRegistrationRequest;
import com.TOM.tom_mini.crm.entity.*;
import com.TOM.tom_mini.crm.repository.AddressRepository;
import com.TOM.tom_mini.crm.repository.CustomerRepository;
import com.TOM.tom_mini.crm.response.CustomerInfoResponse;
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
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, AddressRepository addressRepository,
                           PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Customer registerCustomer(CustomerRegistrationRequest request) {
        String email = request.getEmail();
        Optional<Customer> existingCustomer = customerRepository.findByEmail(email);
        if (existingCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer with email " + email + " already exists.");
        }

        log.info("Registering customer with email: {}", request.getEmail());

        Customer customer = Customer.builder()
                .id(IdGenerator.generate())
                .name(request.getName())
                .surname(request.getSurname())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .birthday(request.getBirthday())
                .email(email)
                .role(Role.valueOf("USER"))
                .created_at(LocalDate.now())
                .modified_at(LocalDate.now())
                .build();

        customer = customerRepository.save(customer);
        Set<Address> Addresses = new HashSet<>();

        for (Address address : request.getAddresses()) {
            address.setCustomer(customer);
            addressRepository.save(address);
        }
        customer.setCustomerAddresses(Addresses);
        log.debug("Customer registration details: {}", request);
        return customer;
    }

    public List<Customer> getAllCustomers() {
        log.info("Fetching all customers from the database");
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(Long id) {
        log.info("Fetching customer with ID: {}", id);
        return customerRepository.findById(id);
    }

    public Optional<Customer> getCustomerByEmail(String email) {
        log.info("Fetching customer with Email: {}", email);
        return customerRepository.findByEmail(email);
    }
}

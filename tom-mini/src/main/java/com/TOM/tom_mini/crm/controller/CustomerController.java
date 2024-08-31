package com.TOM.tom_mini.crm.controller;

import com.TOM.tom_mini.crm.request.CustomerRegistrationRequest;
import com.TOM.tom_mini.crm.response.CustomerInfoResponse;
import com.TOM.tom_mini.crm.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    //Test
    @GetMapping("/public")
    public String sayHello() {
        log.info("Public endpoint accessed: sayHello");
        return "Hello World";
    }

    @GetMapping("/private")
    public String sayPrivateHello() {
        log.info("Private endpoint accessed: sayPrivateHello");
        return "Private zone";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('USER')")
    public String sayAdminHello() {
        log.info("Admin endpoint accessed: sayAdminHello");
        return "Admin zone";
    }

    //Public
    @PostMapping("/public/register")
    public ResponseEntity<CustomerInfoResponse> registerCustomer(@RequestBody CustomerRegistrationRequest request) {
        log.info("Received customer registration request: {}", request);

        CustomerInfoResponse customerInfoResponse = customerService.registerCustomer(request);

        log.info("Successfully registered customer: {}", customerInfoResponse);

        return new ResponseEntity<>(customerInfoResponse, HttpStatus.CREATED);
    }


    //Admin
    @GetMapping("/{id}")
    public ResponseEntity<CustomerInfoResponse> getCustomerById(@PathVariable Long id) {
        log.info("Fetching customer with ID: {}", id);
        try {
            Optional<CustomerInfoResponse> response = customerService.getCustomerResponseById(id);
            if (response.isPresent()) {
                log.info("Customer found: {}", response.get());
                return new ResponseEntity<>(
                        response.get(), HttpStatus.OK);
            } else {
                log.warn("Customer with ID: {} not found", id);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Error fetching customer by ID: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<CustomerInfoResponse>> getAllCustomers() {
        log.info("Fetching all customers");
        try {
            List<CustomerInfoResponse> customers = customerService.getAllCustomers();
            log.info("Found {} customers", customers.size());
            return new ResponseEntity<>(customers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error fetching all customers: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

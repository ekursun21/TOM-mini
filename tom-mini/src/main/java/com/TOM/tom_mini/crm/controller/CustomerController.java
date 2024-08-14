package com.TOM.tom_mini.crm.controller;

import com.TOM.tom_mini.crm.request.CustomerRegistrationRequest;
import com.TOM.tom_mini.crm.entity.Customer;
import com.TOM.tom_mini.crm.service.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/customer")
@Api(value = "Customer Managment System")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    //Testing
    @GetMapping("/public")
    @ApiOperation(value = "public test")
    public String sayHello(){
        return "Hello World";
    }

    @GetMapping("/private")
    @ApiOperation(value = "User private test")
    public String sayPrivateHello(){
        return "Private zone";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Admin private test")
    public String sayAdminHello(){
        return "Admin zone";
    }

    //Public
    @PostMapping("/public/register")
    public ResponseEntity<Customer> registerCustomer(@RequestBody CustomerRegistrationRequest request) {
        log.info("Received customer registration request");
        try {
             Customer savedCustomer = customerService.registerCustomer(request);
            return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();  // Print stack trace for debugging
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Customer Endpoints

    //Admin Endpoints
    @ApiOperation(value = "Get customer by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable UUID id) {
        try {
            Optional<Customer> customer = customerService.getCustomerById(id);
            return customer.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            e.printStackTrace();  // Print stack trace for debugging
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Get all customers")
    @GetMapping("/all")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        try {
            List<Customer> customers = customerService.getAllCustomers();
            return new ResponseEntity<>(customers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_
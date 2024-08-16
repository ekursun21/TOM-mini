package com.TOM.tom_mini.crm.service;

import com.TOM.tom_mini.crm.entity.Customer;
import com.TOM.tom_mini.crm.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class CustomerDetailsServiceImpl implements UserDetailsService {

    private final CustomerService customerService;

    public CustomerDetailsServiceImpl(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading user by username: {}", username);
        Optional<Customer> customer = customerService.getCustomerByEmail(username);
        return customer.orElseThrow(EntityNotFoundException::new);
    }
}

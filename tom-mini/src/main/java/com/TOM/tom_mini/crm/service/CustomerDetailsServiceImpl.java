package com.TOM.tom_mini.crm.service;

import com.TOM.tom_mini.crm.entity.Customer;
import com.TOM.tom_mini.crm.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerDetailsServiceImpl implements UserDetailsService {

    private final CustomerService customerService;

    public CustomerDetailsServiceImpl(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Customer> customer = customerService.getCustomerByEmail(username);
        return customer.orElseThrow(EntityNotFoundException::new);
    }
}

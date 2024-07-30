package com.TOM.tom_mini.crm.service;

import com.TOM.tom_mini.crm.dto.CustomerRegistrationDTO;
import com.TOM.tom_mini.crm.entity.Address;
import com.TOM.tom_mini.crm.entity.Customer;
import com.TOM.tom_mini.crm.entity.CustomerAddress;
import com.TOM.tom_mini.crm.entity.CustomerAddressId;
import com.TOM.tom_mini.crm.repository.AddressRepository;
import com.TOM.tom_mini.crm.repository.CustomerAddressRepository;
import com.TOM.tom_mini.crm.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final CustomerAddressRepository customerAddressRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, AddressRepository addressRepository,
                           CustomerAddressRepository addressAddressRepository) {
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
        this.customerAddressRepository = addressAddressRepository;
    }

    @Transactional
    public Customer registerCustomer(CustomerRegistrationDTO registrationDTO) {
        Customer customer = registrationDTO.toCustomer();
        customer.setCreatedAt(LocalDate.now());
        customer = customerRepository.save(customer);

        for (Address address : registrationDTO.getAddresses()) {
            addressRepository.save(address);

            CustomerAddress customerAddress = new CustomerAddress(new CustomerAddressId(customer.getId(),
                    address.getId()), customer, address);
            customerAddressRepository.save(customerAddress);

            //customer.getCustomerAddresses().add(customerAddress);
        }

        return customer;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(UUID id) {
        return customerRepository.findById(id);
    }
}

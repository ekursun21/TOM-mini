package com.TOM.tom_mini.crm.mapper;

import com.TOM.tom_mini.crm.entity.Customer;
import com.TOM.tom_mini.crm.other.Role;
import com.TOM.tom_mini.crm.request.CustomerRegistrationRequest;
import com.TOM.tom_mini.crm.response.CustomerInfoResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CustomerMapper {

    public CustomerInfoResponse customerToCustomerInfoResponse(Customer customer) {

        return CustomerInfoResponse.builder()
                .name(customer.getName())
                .surname(customer.getSurname())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .role(String.valueOf(customer.getRole()))
                .birthday(customer.getBirthday())
                .created_at(customer.getCreated_at())
                .modified_at(customer.getModified_at())
                .build();
    }

    public Customer customerRegistrationRequestToCustomer (CustomerRegistrationRequest customerRegistrationRequest){
            return Customer.builder()
                    .name(customerRegistrationRequest.getName())
                    .surname(customerRegistrationRequest.getSurname())
                    .email(customerRegistrationRequest.getEmail())
                    .phoneNumber(customerRegistrationRequest.getPhoneNumber())
                    .role(Role.USER)
                    .birthday(customerRegistrationRequest.getBirthday())
                    .created_at(LocalDate.now())
                    .modified_at(LocalDate.now())
                    .build();
        }

}

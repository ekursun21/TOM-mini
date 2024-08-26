package com.TOM.tom_mini.crm.mapper;

import com.TOM.tom_mini.crm.entity.Customer;
import com.TOM.tom_mini.crm.response.CustomerInfoResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public CustomerInfoResponse mapToCustomerInfoResponse(Customer customer) {
        return CustomerInfoResponse.builder()
                .name(customer.getName())
                .surname(customer.getSurname())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .role(customer.getRole().toString())
                .birthday(customer.getBirthday())
                .created_at(customer.getCreatedAt())
                .updated_at(customer.getModifiedAt())
                .build();
    }
}


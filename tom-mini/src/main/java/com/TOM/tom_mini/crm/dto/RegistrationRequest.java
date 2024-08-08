package com.TOM.tom_mini.crm.dto;

import com.TOM.tom_mini.crm.entity.Address;
import com.TOM.tom_mini.crm.entity.Customer;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationRequest {
    private String name;
    private String surname;
    private String password;
    private String phoneNumber;
    private String email;
    private LocalDate birthday;
    private LocalDate createdAt;
    private List<Address> addresses;

    public Customer toCustomer() {
        Customer customer = new Customer();
        customer.setName(this.name);
        customer.setSurname(this.surname);
        customer.setPassword(this.password);  // Consider encrypting the password before saving
        customer.setPhoneNumber(this.phoneNumber);
        customer.setEmail(this.email);
        customer.setBirthday(this.birthday);
        customer.setCreatedAt(this.createdAt);

        return customer;
    }
}


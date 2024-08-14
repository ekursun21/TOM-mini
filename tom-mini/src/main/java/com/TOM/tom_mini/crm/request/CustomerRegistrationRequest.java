package com.TOM.tom_mini.crm.request;

import com.TOM.tom_mini.crm.entity.Address;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
@Getter
@Setter
public class CustomerRegistrationRequest {
    private String name;
    private String surname;
    private String password;
    private String phoneNumber;
    private String email;
    private LocalDate birthday;
    private List<Address> addresses;

}


package com.TOM.tom_mini.crm.config;

import com.TOM.tom_mini.crm.entity.Address;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    private String name;
    private String surname;
    private String password;
    private String phoneNumber;
    private String email;
    private LocalDate birthday;
    private LocalDate createdAt;
    private List<Address> addresses;

}

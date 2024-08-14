package com.TOM.tom_mini.crm.response;

import com.TOM.tom_mini.crm.entity.Address;
import com.TOM.tom_mini.crm.entity.Role;

import java.time.LocalDate;
import java.util.List;

public class CustomerInfoResponse {
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private String role;
    private LocalDate birthday;
    private LocalDate created_at;
    private LocalDate updated_at;
}

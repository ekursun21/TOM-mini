package com.TOM.tom_mini.crm.response;

import io.swagger.models.Contact;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

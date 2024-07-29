package com.TOM.tom_mini.crm.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "customeraddress")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAddress {
    @EmbeddedId
    private CustomerAddressId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("customerId")
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("addressId")
    @JoinColumn(name = "address_id")
    private Address address;


    // Additional fields can be added here
}


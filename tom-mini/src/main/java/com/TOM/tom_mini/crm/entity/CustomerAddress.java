package com.TOM.tom_mini.crm.entity;

import com.TOM.tom_mini.crm.other.CustomerAddressId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customer_address")
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


}


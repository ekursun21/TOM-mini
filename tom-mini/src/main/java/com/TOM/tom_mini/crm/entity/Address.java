package com.TOM.tom_mini.crm.entity;

import com.TOM.tom_mini.crm.other.IdGenerator;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "address")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private Long id = IdGenerator.generate();

    private String streetAddress;
    private String city;
    private String state;
    private String postalCode;
    private int countryId;
    private String building;
    private String addressLine;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
}

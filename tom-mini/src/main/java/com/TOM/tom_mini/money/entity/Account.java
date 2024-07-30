package com.TOM.tom_mini.money.entity;

import com.TOM.tom_mini.crm.entity.Customer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "accounts")
@Getter
@Setter
public class Account {

    @Id
    @Column(name = "account_no")
    private String accountNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "account_type")
    private String accountType;

    @Column(name = "balance", precision = 19, scale = 4)
    private BigDecimal balance;

    @Column(name = "created_at")
    private LocalDate createdAt;

}
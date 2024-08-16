package com.TOM.tom_mini.money.entity;

import com.TOM.tom_mini.crm.entity.Customer;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Builder
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @Column(name = "account_no")
    private Long accountNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "account_type")
    private String accountType;

    @Column(name = "balance", precision = 19, scale = 4)
    private BigDecimal balance;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @OneToMany(mappedBy = "fromAccount")
    private Set<Transaction> outgoingTransactions;

    @OneToMany(mappedBy = "toAccount")
    private Set<Transaction> incomingTransactions;

    public void credit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public void debit(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
    }
}
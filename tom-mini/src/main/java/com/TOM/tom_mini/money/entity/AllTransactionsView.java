package com.TOM.tom_mini.money.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Immutable
@Table(name = "all_transactions_view")
public class AllTransactionsView {

    @Id
    private UUID id;

    @Column(name = "from_account_no")
    private String fromAccountNo;

    @Column(name = "to_account_no")
    private String toAccountNo;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "description")
    private String description;

    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;

    @Column(name = "transaction_type")
    private String transactionType;

}


package com.TOM.tom_mini.money.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class AccountDTO {
    private UUID customerId; // Reference to customer
    private String accountType;
    private BigDecimal balance;

    // Getters and setters
}

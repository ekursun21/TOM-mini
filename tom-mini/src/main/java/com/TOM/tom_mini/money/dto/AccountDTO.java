package com.TOM.tom_mini.money.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountDTO {
    private Long customerId; // Reference to customer
    private String accountType;
    private BigDecimal balance;

    // Getters and setters
}

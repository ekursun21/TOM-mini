package com.TOM.tom_mini.money.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class MonthlyTransactionSummaryDTO {
    private int year;
    private int month;
    private BigDecimal totalAmount;
    private long transactionCount;

}


package com.TOM.tom_mini.money.request;

import com.TOM.tom_mini.money.entity.TransactionType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransactionCreateRequest {
    private Long fromAccountNo;
    private Long toAccountNo;
    private TransactionType transactionType;
    private BigDecimal amount;
    private String description;
}

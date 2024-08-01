package com.TOM.tom_mini.money.dto;

import com.TOM.tom_mini.money.entity.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferDTO {

    private String from_accountNo;
    private String to_accountNo;
    private TransactionType transactionType;
    private BigDecimal amount;
    private String description;

}

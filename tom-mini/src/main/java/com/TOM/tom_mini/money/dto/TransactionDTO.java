package com.TOM.tom_mini.money.dto;

import java.math.BigDecimal;

import com.TOM.tom_mini.money.entity.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.resource.transaction.spi.TransactionStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {

    private String accountNo;
    private TransactionType transactionType;
    private BigDecimal amount;
    private String description;


}


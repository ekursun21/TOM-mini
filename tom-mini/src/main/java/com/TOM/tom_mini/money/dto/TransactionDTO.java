package com.TOM.tom_mini.money.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.TOM.tom_mini.money.entity.TransactionType;
import lombok.*;
import org.hibernate.resource.transaction.spi.TransactionStatus;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {

    private Long fromAccountNo;
    private Long toAccountNo;
    private TransactionType transactionType;
    private BigDecimal amount;
    private String description;
    private LocalDate transactionDate;


}


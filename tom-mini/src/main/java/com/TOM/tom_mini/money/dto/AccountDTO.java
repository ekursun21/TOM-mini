package com.TOM.tom_mini.money.dto;

import lombok.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    private Long customerId;
    private String accountType;
    private BigDecimal balance;
    private LocalDate createdAt;
}

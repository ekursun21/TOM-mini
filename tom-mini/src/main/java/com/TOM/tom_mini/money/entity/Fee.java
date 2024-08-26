package com.TOM.tom_mini.money.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Fee {
    @Id
    private Long id;
    private String feeType;
    private String accountType;
    private BigDecimal amount;
    private LocalDateTime updated_at;
}

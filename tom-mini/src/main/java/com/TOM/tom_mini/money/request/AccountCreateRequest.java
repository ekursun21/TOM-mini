package com.TOM.tom_mini.money.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountCreateRequest {
    private Long customerId;
    private String accountType;
    private BigDecimal balance;;
}

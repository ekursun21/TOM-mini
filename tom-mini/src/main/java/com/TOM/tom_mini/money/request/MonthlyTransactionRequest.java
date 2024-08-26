package com.TOM.tom_mini.money.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyTransactionRequest {
    private Long accountNo;
    private int year;
    private int month;
}

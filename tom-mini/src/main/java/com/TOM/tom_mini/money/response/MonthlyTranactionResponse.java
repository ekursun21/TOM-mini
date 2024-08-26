package com.TOM.tom_mini.money.response;

import com.TOM.tom_mini.money.dto.TransactionDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyTranactionResponse {
    private String message;
    private int statusCode;
    private List<TransactionDTO> transactions;
}

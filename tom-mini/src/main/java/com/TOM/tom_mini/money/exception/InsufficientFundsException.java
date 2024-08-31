package com.TOM.tom_mini.money.exception;

import java.math.BigDecimal;

public class InsufficientFundsException extends RuntimeException{
    public InsufficientFundsException(Long accountNo, BigDecimal currentBalance, BigDecimal requiredAmount) {
        super("Account with number " + accountNo + " has insufficient funds. Current balance: " + currentBalance + ", required: " + requiredAmount);
    }}

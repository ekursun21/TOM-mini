package com.TOM.tom_mini.money.exception;

public class AccountNotFoundException extends RuntimeException{
    public AccountNotFoundException(String account_no){
        super("Account with number "+account_no+" could not be found");
    }
}

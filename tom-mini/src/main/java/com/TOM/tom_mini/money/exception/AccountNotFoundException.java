package com.TOM.tom_mini.money.exception;

public class AccountNotFoundException extends RuntimeException{
    public AccountNotFoundException(Long account_no){
        super("Account with number "+account_no+" could not be found");
    }
    public AccountNotFoundException(String accountNo) {
        super("Account with number " + accountNo + " not found.");
    }
}

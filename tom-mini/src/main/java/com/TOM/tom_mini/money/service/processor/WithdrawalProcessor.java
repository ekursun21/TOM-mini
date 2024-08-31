package com.TOM.tom_mini.money.service.processor;

import com.TOM.tom_mini.money.entity.Account;
import com.TOM.tom_mini.money.entity.Transaction;
import com.TOM.tom_mini.money.exception.InsufficientFundsException;
import com.TOM.tom_mini.money.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WithdrawalProcessor implements TransactionProcessor {
    private final AccountRepository accountRepository;

    @Autowired
    public WithdrawalProcessor(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void process(Transaction transaction) {
        BigDecimal transactionAmount = transaction.getAmount();
        Account fromAccount = transaction.getFromAccount();
        if (fromAccount.getBalance().compareTo(transactionAmount) < 0) {
            throw new InsufficientFundsException(fromAccount.getAccountNo(), fromAccount.getBalance(), transactionAmount);
        }
        fromAccount.debit(transactionAmount);
        accountRepository.save(fromAccount);
    }
}

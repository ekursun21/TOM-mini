package com.TOM.tom_mini.money.service.processor;

import com.TOM.tom_mini.money.dto.TransactionDTO;
import com.TOM.tom_mini.money.entity.Account;
import com.TOM.tom_mini.money.entity.Transaction;
import com.TOM.tom_mini.money.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DepositProcessor implements TransactionProcessor {
    private final AccountRepository accountRepository;

    @Autowired
    public DepositProcessor(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void process(Transaction transaction) {
        BigDecimal transactionAmount = transaction.getAmount();
        Account fromAccount = transaction.getFromAccount();
        fromAccount.credit(transactionAmount);
        accountRepository.save(fromAccount);
    }
}
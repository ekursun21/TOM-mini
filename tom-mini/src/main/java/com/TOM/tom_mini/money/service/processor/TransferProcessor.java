package com.TOM.tom_mini.money.service.processor;

import com.TOM.tom_mini.money.entity.Account;
import com.TOM.tom_mini.money.entity.Transaction;
import com.TOM.tom_mini.money.exception.InsufficientFundsException;
import com.TOM.tom_mini.money.exception.VaultAccountNotFoundException;
import com.TOM.tom_mini.money.repository.AccountRepository;
import com.TOM.tom_mini.money.service.FeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransferProcessor implements TransactionProcessor {
    private final AccountRepository accountRepository;
    private final FeeService feeService;

    @Autowired
    public TransferProcessor(AccountRepository accountRepository, FeeService feeService) {
        this.accountRepository = accountRepository;
        this.feeService = feeService;
    }

    @Override
    public void process(Transaction transaction) {
        Account toAccount = transaction.getToAccount();
        Account fromAccount = transaction.getFromAccount();
        BigDecimal transactionAmount = transaction.getAmount();
        String accountType = fromAccount.getAccountType();
        BigDecimal fee = transactionAmount.compareTo(BigDecimal.valueOf(10000)) > 0
                ? feeService.getFee("HIGH_VALUE", accountType)
                : feeService.getFee("LOW_VALUE", accountType);

        BigDecimal totalAmount = transactionAmount.add(fee);
        if (fromAccount.getBalance().compareTo(totalAmount) < 0) {
            throw new InsufficientFundsException(fromAccount.getAccountNo(), fromAccount.getBalance(), totalAmount);
        }

        Account vaultAccount = getBankVaultAccount();
        fromAccount.debit(totalAmount);
        toAccount.credit(transactionAmount);
        vaultAccount.credit(fee);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
        accountRepository.save(vaultAccount);
    }

    private Account getBankVaultAccount() {
        return accountRepository.findByAccountType("Vault")
                .orElseThrow(VaultAccountNotFoundException::new);
    }
}

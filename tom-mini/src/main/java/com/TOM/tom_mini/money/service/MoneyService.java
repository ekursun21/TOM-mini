package com.TOM.tom_mini.money.service;

import com.TOM.tom_mini.crm.repository.CustomerRepository;
import com.TOM.tom_mini.money.dto.MonthlyTransactionSummaryDTO;
import com.TOM.tom_mini.money.dto.TransactionDTO;
import com.TOM.tom_mini.money.entity.*;
import com.TOM.tom_mini.money.exception.AccountNotFoundException;
import com.TOM.tom_mini.money.repository.AccountRepository;
import com.TOM.tom_mini.money.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MoneyService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public MoneyService(TransactionRepository transactionRepository, AccountRepository accountRepository,
                        CustomerRepository customerRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
    }


    @Transactional
    public Transaction processTransaction(TransactionDTO transactionDTO) {
        Account fromAccount = accountRepository.findById(transactionDTO.getFromAccountNo())
                .orElseThrow(() -> new IllegalArgumentException("From account not found"));

        Account toAccount = null;
        if (transactionDTO.getToAccountNo() != null) {
            toAccount = accountRepository.findById(transactionDTO.getToAccountNo())
                    .orElseThrow(() -> new IllegalArgumentException("To account not found"));
        }

        Transaction transaction = new Transaction();
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setTransactionType(transactionDTO.getTransactionType());
        transaction.setTransactionTime(LocalDateTime.now());
        transaction.setDescription(transactionDTO.getDescription());

        adjustAccountBalances(fromAccount, toAccount, transactionDTO.getAmount(), transactionDTO.getTransactionType());
        transactionRepository.save(transaction);
        return transaction;
    }

    private void adjustAccountBalances(Account fromAccount, Account toAccount, BigDecimal amount, TransactionType transactionType) {
        switch (transactionType) {
            case DEPOSIT:
                fromAccount.credit(amount);
                break;
            case WITHDRAWAL:
                fromAccount.debit(amount);
                break;
            case TRANSFER:
                if (toAccount == null) {
                    throw new IllegalArgumentException("To account cannot be null for transfers");
                }
                if (fromAccount.getBalance().compareTo(amount) < 0) {
                    throw new IllegalArgumentException("Insufficient funds");
                }
                fromAccount.debit(amount);
                toAccount.credit(amount);
                break;
            default:
                throw new IllegalStateException("Unexpected transaction type: " + transactionType);
        }

        accountRepository.save(fromAccount);
        if (toAccount != null) {
            accountRepository.save(toAccount);
        }
    }

    public List<Transaction> getTransactionsForAccount(String accountNo) {
        return transactionRepository.findAll(); // Simplified for example
    }

}

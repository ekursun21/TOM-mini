package com.TOM.tom_mini.money.service;

import com.TOM.tom_mini.crm.entity.Customer;
import com.TOM.tom_mini.crm.repository.CustomerRepository;
import com.TOM.tom_mini.money.dto.AccountDTO;
import com.TOM.tom_mini.money.dto.MonthlyTransactionSummaryDTO;
import com.TOM.tom_mini.money.dto.TransactionDTO;
import com.TOM.tom_mini.money.dto.TransferDTO;
import com.TOM.tom_mini.money.entity.*;
import com.TOM.tom_mini.money.exception.AccountNotFoundException;
import com.TOM.tom_mini.money.repository.AccountRepository;
import com.TOM.tom_mini.money.repository.AllTransactionsViewRepository;
import com.TOM.tom_mini.money.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MoneyService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final AllTransactionsViewRepository allTransactionsViewRepository;

    @Autowired
    public MoneyService(TransactionRepository transactionRepository, AccountRepository accountRepository,
                        CustomerRepository customerRepository,
                        AllTransactionsViewRepository allTransactionsViewRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.allTransactionsViewRepository = allTransactionsViewRepository;
    }


    @Transactional
    public Transaction recordTransaction(TransactionDTO transactionDTO) {
        Account account = accountRepository.findByAccountNo(transactionDTO.getAccountNo())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setTransactionType(transactionDTO.getTransactionType());
        transaction.setTransactionTime(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transfer recordTransfer(TransferDTO transferDTO) {
        Account from_account = accountRepository.findById(transferDTO.getFrom_accountNo())
                .orElseThrow(() -> new AccountNotFoundException(transferDTO.getFrom_accountNo()));

        Account to_account = accountRepository.findById(transferDTO.getTo_accountNo())
                .orElseThrow(() -> new AccountNotFoundException(transferDTO.getTo_accountNo()));

        BigDecimal amount = transferDTO.getAmount();

        if (from_account.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        // Debit the sender's account and credit the receiver's account
        from_account.debit(amount);
        to_account.credit(amount);

        // Persist the updated account states
        accountRepository.save(from_account);
        accountRepository.save(to_account);

        Transfer transfer = new Transfer();
        transfer.setAmount(transferDTO.getAmount());
        transfer.setFromAccount(from_account);
        transfer.setToAccount(to_account);
        transfer.setDescription(transferDTO.getDescription());
        transfer.setTransactionTime(LocalDateTime.now());
        return transfer;
    }

    public List<Transaction> getTransactionsForAccount(String accountNo) {
        return transactionRepository.findAll(); // Simplified for example
    }

    public List<MonthlyTransactionSummaryDTO> getMonthlyTransactionSummaries() {
        return allTransactionsViewRepository.findMonthlyTransactionSummaries();
    }
}

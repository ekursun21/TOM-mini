package com.TOM.tom_mini.money.service;

import com.TOM.tom_mini.crm.other.IdGenerator;
import com.TOM.tom_mini.money.dto.AccountDTO;
import com.TOM.tom_mini.money.dto.TransactionDTO;
import com.TOM.tom_mini.money.entity.Account;
import com.TOM.tom_mini.money.entity.Transaction;
import com.TOM.tom_mini.money.repository.AccountRepository;
import com.TOM.tom_mini.crm.entity.Customer;
import com.TOM.tom_mini.crm.repository.CustomerRepository;
import com.TOM.tom_mini.money.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository, CustomerRepository customerRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Account createAccount(AccountDTO accountDTO) {
        log.info("Creating account for customer ID: {}", accountDTO.getCustomerId());

        Optional<Customer> customer = customerRepository.findById(accountDTO.getCustomerId());
        if (!customer.isPresent()) {
            log.error("Invalid customer ID: {}", accountDTO.getCustomerId());
            throw new IllegalArgumentException("Invalid customer ID");
        }

        Account account = new Account();
        account.setCustomer(customer.get());
        account.setAccountType(accountDTO.getAccountType());
        account.setBalance(accountDTO.getBalance() != null ? accountDTO.getBalance() : BigDecimal.ZERO);
        account.setCreatedAt(LocalDate.now());
        account.setAccountNo(IdGenerator.generate());

        Account savedAccount = accountRepository.save(account);
        log.info("Account created successfully with account number: {}", savedAccount.getAccountNo());
        return savedAccount;
    }

    public Account findByAccountNo(Long accountNo) {
        log.info("Finding account by account number: {}", accountNo);
        return accountRepository.findByAccountNo(accountNo).orElseThrow(() -> {
            log.error("Account with account number {} not found", accountNo);
            return new IllegalArgumentException("Account with account number " + accountNo + " not found");
        });
    }

    @Transactional
    public Account updateAccount(Long accountNo, AccountDTO accountDTO) {
        log.info("Updating account with account number: {}", accountNo);

        Account account = accountRepository.findByAccountNo(accountNo)
                .orElseThrow(() -> {
                    log.error("Account with account number {} not found", accountNo);
                    return new IllegalArgumentException("Account with account number " + accountNo + " not found");
                });

        account.setAccountType(accountDTO.getAccountType());
        account.setBalance(accountDTO.getBalance());

        Account updatedAccount = accountRepository.save(account);
        log.info("Account updated successfully with account number: {}", updatedAccount.getAccountNo());
        return updatedAccount;
    }

    @Transactional
    public void deleteAccount(Long accountNo) {
        log.info("Deleting account with account number: {}", accountNo);

        Account account = accountRepository.findByAccountNo(accountNo)
                .orElseThrow(() -> {
                    log.error("Account with account number {} not found", accountNo);
                    return new IllegalArgumentException("Account with account number " + accountNo + " not found");
                });

        accountRepository.delete(account);
        log.info("Account with account number {} deleted successfully", accountNo);
    }

    public List<Account> getAllAccounts() {
        log.info("Fetching all accounts");
        List<Account> accounts = accountRepository.findAll();
        log.info("Found {} accounts", accounts.size());
        return accounts;
    }

    public List<TransactionDTO> getMonthlyTransactions(Long accountNo, int year, int month) {
        log.info("Fetching monthly transactions for account number: {} for year: {} and month: {}", accountNo, year, month);

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        Account account = accountRepository.findById(accountNo)
                .orElseThrow(() -> {
                    log.error("Invalid account ID: {}", accountNo);
                    return new IllegalArgumentException("Invalid account ID");
                });

        List<Transaction> transactions = transactionRepository.findByFromAccountAndTransactionTimeBetween(account, startDate, endDate);
        log.info("Found {} transactions for account number: {}", transactions.size(), accountNo);

        return transactions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private TransactionDTO convertToDto(Transaction transaction) {
        log.debug("Converting transaction to DTO: {}", transaction);
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .amount(transaction.getAmount())
                .toAccountNo(transaction.getToAccount().getAccountNo())
                .transactionType(transaction.getTransactionType())
                .description(transaction.getDescription())
                .fromAccountNo(transaction.getFromAccount().getAccountNo())
                .transactionDate(transaction.getTransactionTime())
                .build();
        log.debug("TransactionDTO created: {}", transactionDTO);
        return transactionDTO;
    }
}
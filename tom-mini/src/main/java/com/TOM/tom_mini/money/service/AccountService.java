package com.TOM.tom_mini.money.service;

import com.TOM.tom_mini.money.dto.AccountDTO;
import com.TOM.tom_mini.money.dto.TransactionDTO;
import com.TOM.tom_mini.money.entity.Account;
import com.TOM.tom_mini.money.entity.Transaction;
import com.TOM.tom_mini.money.mapper.AccountMapper;
import com.TOM.tom_mini.money.mapper.TransactionMapper;
import com.TOM.tom_mini.money.repository.AccountRepository;
import com.TOM.tom_mini.crm.entity.Customer;
import com.TOM.tom_mini.crm.repository.CustomerRepository;
import com.TOM.tom_mini.money.repository.TransactionRepository;
import com.TOM.tom_mini.money.request.AccountCreateRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private final AccountMapper accountMapper;
    private final TransactionMapper transactionMapper;

    @Autowired
    public AccountService(AccountRepository accountRepository, CustomerRepository customerRepository,
                          TransactionRepository transactionRepository, AccountMapper accountMapper, TransactionMapper transactionMapper) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
        this.accountMapper = accountMapper;
        this.transactionMapper = transactionMapper;
    }

    @Transactional
    public AccountDTO createAccount(AccountCreateRequest request) {
        log.info("Creating account for customer ID: {}", request.getCustomerId());

        Optional<Customer> customer = customerRepository.findById(request.getCustomerId());
        if (!customer.isPresent()) {
            log.error("Invalid customer ID: {}", request.getCustomerId());
            throw new IllegalArgumentException("Invalid customer ID");
        }

        Account savedAccount = accountRepository.save(accountMapper.accountCreateRequestToAccount(request));
        log.info("Account created successfully with account number: {}", savedAccount.getAccountNo());

        return accountMapper.accountToAccountDto(savedAccount);
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

    public List<AccountDTO> getAllAccounts() {
        log.info("Fetching all accounts");
        List<Account> accounts = accountRepository.findAll();
        log.info("Found {} accounts", accounts.size());
        return accounts.stream()
                .map(accountMapper::accountToAccountDto)
                .collect(Collectors.toList());
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
                .map(transactionMapper::transactionToTransactionDTO)
                .collect(Collectors.toList());
    }
}
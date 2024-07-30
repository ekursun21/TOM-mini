package com.TOM.tom_mini.money.service;

import com.TOM.tom_mini.money.dto.AccountDTO;
import com.TOM.tom_mini.money.entity.Account;
import com.TOM.tom_mini.money.entity.AccountNumberGenerator;
import com.TOM.tom_mini.money.repository.AccountRepository;
import com.TOM.tom_mini.crm.entity.Customer;
import com.TOM.tom_mini.crm.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository, CustomerRepository customerRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public Account createAccount(AccountDTO accountDTO) {
        Optional<Customer> customer = customerRepository.findById(accountDTO.getCustomerId());
        if (!customer.isPresent()) {
            throw new IllegalArgumentException("Invalid customer ID");
        }

        Account account = new Account();
        account.setCustomer(customer.get());
        account.setAccountType(accountDTO.getAccountType());
        account.setBalance(accountDTO.getBalance() != null ? accountDTO.getBalance() : BigDecimal.ZERO);
        account.setCreatedAt(LocalDate.now());
        account.setAccountNo(AccountNumberGenerator.generate());
        return accountRepository.save(account);

    }

    public Account findByAccountNo(String accountNo) {
        return accountRepository.findByAccountNo(accountNo).orElseThrow(() ->
                new IllegalArgumentException("Account with account number " + accountNo + " not found"));
    }

    @Transactional
    public Account updateAccount(String accountNo, AccountDTO accountDTO) {
        Account account = accountRepository.findByAccountNo(accountNo)
                .orElseThrow(() -> new IllegalArgumentException("Account with account number " + accountNo + " not found"));

        account.setAccountType(accountDTO.getAccountType());
        account.setBalance(accountDTO.getBalance());
        return accountRepository.save(account);
    }

    @Transactional
    public void deleteAccount(String accountNo) {
        Account account = accountRepository.findByAccountNo(accountNo)
                .orElseThrow(() -> new IllegalArgumentException("Account with account number " + accountNo + " not found"));
        accountRepository.delete(account);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
}

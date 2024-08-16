package com.TOM.tom_mini.money.controller;

import com.TOM.tom_mini.money.dto.AccountDTO;
import com.TOM.tom_mini.money.dto.TransactionDTO;
import com.TOM.tom_mini.money.entity.Account;
import com.TOM.tom_mini.money.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create-account")
    public ResponseEntity<Account> createAccount(@RequestBody AccountDTO accountDTO) {
        log.info("Received request to create account for customer ID: {}", accountDTO.getCustomerId());
        try {
            Account newAccount = accountService.createAccount(accountDTO);
            log.info("Account created successfully with account number: {}", newAccount.getAccountNo());
            return new ResponseEntity<>(newAccount, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error occurred while creating account for customer ID: {}", accountDTO.getCustomerId(), e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{account_no}/monthly-transactions")
    public ResponseEntity<List<TransactionDTO>> getMonthlyTransactions(
            @PathVariable("account_no") Long accountNo,
            @RequestParam("year") int year,
            @RequestParam("month") int month) {
        log.info("Received request to fetch monthly transactions for account number: {} for year: {} and month: {}", accountNo, year, month);
        try {
            List<TransactionDTO> transactions = accountService.getMonthlyTransactions(accountNo, year, month);
            log.info("Successfully retrieved {} transactions for account number: {}", transactions.size(), accountNo);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            log.error("Error occurred while fetching monthly transactions for account number: {}", accountNo, e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Account>> getAllAccounts() {
        log.info("Received request to fetch all accounts");
        try {
            List<Account> accounts = accountService.getAllAccounts();
            log.info("Successfully retrieved {} accounts", accounts.size());
            return new ResponseEntity<>(accounts, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while fetching all accounts", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
package com.TOM.tom_mini.money.controller;

import com.TOM.tom_mini.money.dto.TransactionDTO;
import com.TOM.tom_mini.money.entity.Transaction;
import com.TOM.tom_mini.money.service.MoneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class MoneyController {

    private final MoneyService moneyService;

    @Autowired
    public MoneyController(MoneyService moneyService) {
        this.moneyService = moneyService;
    }

    @PostMapping("/create")
    public ResponseEntity<Transaction> createTransaction(@RequestBody TransactionDTO transactionDTO) {

            Transaction transaction = moneyService.processTransaction(transactionDTO);
            return new ResponseEntity<>(transaction, HttpStatus.CREATED);

    }

    @GetMapping("/account/{accountNo}")
    public ResponseEntity<List<Transaction>> getTransactionsByAccount(@PathVariable String accountNo) {
        try {
            List<Transaction> transactions = moneyService.getTransactionsForAccount(accountNo);
            if (transactions.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(transactions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

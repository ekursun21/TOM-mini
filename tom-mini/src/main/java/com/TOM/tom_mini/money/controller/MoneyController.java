package com.TOM.tom_mini.money.controller;

import com.TOM.tom_mini.money.dto.TransactionDTO;
import com.TOM.tom_mini.money.request.TransactionCreateRequest;
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
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody TransactionCreateRequest request) {
            TransactionDTO transactionDto = moneyService.processTransaction(request);
            return new ResponseEntity<>(transactionDto, HttpStatus.CREATED);
    }

    @GetMapping("/account/{accountNo}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByAccount(@PathVariable String accountNo) {
        try {
            List<TransactionDTO> transactions = moneyService.getTransactionsForAccount(accountNo);
            if (transactions.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(transactions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

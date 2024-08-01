package com.TOM.tom_mini.money.controller;

import com.TOM.tom_mini.money.dto.MonthlyTransactionSummaryDTO;
import com.TOM.tom_mini.money.dto.TransactionDTO;
import com.TOM.tom_mini.money.dto.TransferDTO;
import com.TOM.tom_mini.money.entity.Transaction;
import com.TOM.tom_mini.money.entity.Transfer;
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

    @PostMapping("/create-transaction")
    public ResponseEntity<Transaction> createTransaction(@RequestBody TransactionDTO transactionDTO) {
        try {
            Transaction transaction = moneyService.recordTransaction(transactionDTO);
            return new ResponseEntity<>(transaction, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create-transfer")
    public ResponseEntity<Transfer> createTransfer(@RequestBody TransferDTO transferDTO) {
        try {
            Transfer transfer = moneyService.recordTransfer(transferDTO);
            return new ResponseEntity<>(transfer, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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

    @GetMapping("/summary/monthly")
    public ResponseEntity<List<MonthlyTransactionSummaryDTO>> getMonthlyTransactionSummaries() {
        List<MonthlyTransactionSummaryDTO> summaries = moneyService.getMonthlyTransactionSummaries();
        return ResponseEntity.ok(summaries);
    }

}

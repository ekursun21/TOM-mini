package com.TOM.tom_mini.money.service;

import com.TOM.tom_mini.crm.other.IdGenerator;
import com.TOM.tom_mini.money.dto.TransactionDTO;
import com.TOM.tom_mini.money.entity.*;
import com.TOM.tom_mini.money.exception.AccountNotFoundException;
import com.TOM.tom_mini.money.exception.TransactionProcessingException;
import com.TOM.tom_mini.money.mapper.TransactionMapper;
import com.TOM.tom_mini.money.repository.AccountRepository;
import com.TOM.tom_mini.money.repository.TransactionRepository;
import com.TOM.tom_mini.money.request.TransactionCreateRequest;
import com.TOM.tom_mini.money.service.processor.TransactionProcessor;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MoneyService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final Map<TransactionType, TransactionProcessor> transactionProcessors;
    private final TransactionMapper transactionMapper;

    @Autowired
    public MoneyService(AccountRepository accountRepository,
                        TransactionRepository transactionRepository,
                        Map<TransactionType, TransactionProcessor> transactionProcessors,
                        TransactionMapper transactionMapper) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.transactionProcessors = transactionProcessors;
        this.transactionMapper = transactionMapper;
    }

    @Transactional
    public TransactionDTO processTransaction(TransactionCreateRequest request) {
        log.info("Processing transaction: {}", request);

        Account fromAccount = accountRepository.findById(request.getFromAccountNo())
                .orElseThrow(() -> {
                    log.error("From account not found: {}", request.getFromAccountNo());
                    return new AccountNotFoundException(request.getFromAccountNo());
                });

        Transaction transaction = transactionMapper.transactionCreateRequestToTransaction(request);
        transaction.setFromAccount(accountRepository.findByAccountNo(request.getFromAccountNo())
                .orElseThrow(() -> new AccountNotFoundException(request.getFromAccountNo())));
        transaction.setFromAccount(accountRepository.findByAccountNo(request.getFromAccountNo())
                .orElseThrow(() -> new AccountNotFoundException(request.getFromAccountNo())));

        TransactionProcessor processor = transactionProcessors.get(request.getTransactionType());
        if (processor == null) {
            throw new TransactionProcessingException("No processor found for transaction type: " + request.getTransactionType());
        }

        processor.process(transaction);

        transactionRepository.save(transaction);
        log.info("Transaction processed successfully with ID: {}", transaction.getId());

        return transactionMapper.transactionToTransactionDTO(transaction);
    }

    public List<TransactionDTO> getTransactionsForAccount(String accountNo) {
        log.info("Fetching transactions for account: {}", accountNo);
        List<Transaction> transactions = transactionRepository.findAll(); // Simplified for example
        log.info("Found {} transactions for account: {}", transactions.size(), accountNo);
        return transactions.stream().map(transactionMapper::transactionToTransactionDTO).collect(Collectors.toList());
    }
}

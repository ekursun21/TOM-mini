package com.TOM.tom_mini.money.service;

import com.TOM.tom_mini.crm.other.IdGenerator;
import com.TOM.tom_mini.money.dto.TransactionDTO;
import com.TOM.tom_mini.money.entity.*;
import com.TOM.tom_mini.money.mapper.TransactionMapper;
import com.TOM.tom_mini.money.repository.AccountRepository;
import com.TOM.tom_mini.money.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class MoneyService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final FeeService feeService;

    @Autowired
    public MoneyService(TransactionRepository transactionRepository, AccountRepository accountRepository,
                        FeeService feeService) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.feeService = feeService;
    }

    @Transactional
    public Transaction processTransaction(TransactionDTO transactionDTO) {
        log.info("Processing transaction: {}", transactionDTO);

        Account fromAccount = accountRepository.findById(transactionDTO.getFromAccountNo())
                .orElseThrow(() -> {
                    log.error("From account not found: {}", transactionDTO.getFromAccountNo());
                    return new IllegalArgumentException("From account not found");
                });

        Account toAccount = null;
        if (transactionDTO.getToAccountNo() != null) {
            toAccount = accountRepository.findById(transactionDTO.getToAccountNo())
                    .orElseThrow(() -> {
                        log.error("To account not found: {}", transactionDTO.getToAccountNo());
                        return new IllegalArgumentException("To account not found");
                    });
        }

        Transaction transaction = TransactionMapper.INSTANCE.toTransaction(transactionDTO);
        transaction.setId(IdGenerator.generate());
        transaction.setTransactionTime(LocalDate.now());


        log.debug("Created transaction object: {}", transaction);

        String accountType = fromAccount.getAccountType();
        BigDecimal transactionAmount = transactionDTO.getAmount();


        if (transactionDTO.getTransactionType() == TransactionType.TRANSFER) {
            BigDecimal fee = transactionAmount.compareTo(BigDecimal.valueOf(10000)) > 0
                    ? feeService.getFee("HIGH_VALUE", accountType)
                    : feeService.getFee("LOW_VALUE", accountType);

            BigDecimal totalAmount = transactionAmount.add(fee);

            if (fromAccount.getBalance().compareTo(totalAmount) < 0) {
                log.error("Insufficient funds in account: {} for transaction amount: {}", fromAccount.getAccountNo(), totalAmount);
                throw new IllegalArgumentException("Insufficient funds");
            }

            log.info("Processing transfer with fee. From account: {}, To account: {}, Amount: {}, Fee: {}",
                    fromAccount.getAccountNo(), toAccount != null ? toAccount.getAccountNo() : "N/A", transactionAmount, fee);

            Account vaultAccount = getBankVaultAccount();

            fromAccount.debit(totalAmount);
            if (toAccount != null) {
                toAccount.credit(transactionAmount);
            }
            vaultAccount.credit(fee);

            accountRepository.save(fromAccount);
            if (toAccount != null) {
                accountRepository.save(toAccount);
            }
            accountRepository.save(vaultAccount);
        } else {
            adjustAccountBalances(fromAccount, toAccount, transactionAmount, transactionDTO.getTransactionType());
        }

        transactionRepository.save(transaction);
        log.info("Transaction processed successfully: {}", transaction);
        return transaction;
    }

    private Account getBankVaultAccount() {
        log.info("Fetching bank's vault account");
        return accountRepository.findByAccountType("Vault")
                .orElseThrow(() -> {
                    log.error("Bank's vault account not found");
                    return new IllegalStateException("Bank's vault account not found");
                });
    }

    private void adjustAccountBalances(Account fromAccount, Account toAccount, BigDecimal amount, TransactionType transactionType) {
        log.info("Adjusting account balances. Transaction type: {}", transactionType);
        switch (transactionType) {
            case DEPOSIT:
                log.info("Depositing amount: {} to account: {}", amount, fromAccount.getAccountNo());
                fromAccount.credit(amount);
                break;
            case WITHDRAWAL:
                log.info("Withdrawing amount: {} from account: {}", amount, fromAccount.getAccountNo());
                if (fromAccount.getBalance().compareTo(amount) < 0) {
                    log.error("Insufficient funds in account: {}", fromAccount.getAccountNo());
                    throw new IllegalArgumentException("Insufficient funds");
                }
                fromAccount.debit(amount);
                break;
            case TRANSFER:
                log.info("Transferring amount: {} from account: {} to account: {}", amount, fromAccount.getAccountNo(), toAccount.getAccountNo());
                if (fromAccount.getBalance().compareTo(amount) < 0) {
                    log.error("Insufficient funds in account: {}", fromAccount.getAccountNo());
                    throw new IllegalArgumentException("Insufficient funds");
                }
                fromAccount.debit(amount);
                toAccount.credit(amount);
                break;
            default:
                log.error("Unexpected transaction type: {}", transactionType);
                throw new IllegalStateException("Unexpected transaction type: " + transactionType);
        }

        accountRepository.save(fromAccount);
        if (toAccount != null) {
            accountRepository.save(toAccount);
        }
        log.info("Account balances adjusted successfully.");
    }

    public List<Transaction> getTransactionsForAccount(String accountNo) {
        log.info("Fetching transactions for account: {}", accountNo);
        List<Transaction> transactions = transactionRepository.findAll(); // Simplified for example
        log.info("Found {} transactions for account: {}", transactions.size(), accountNo);
        return transactions;
    }
}
package com.TOM.tom_mini.money;

import com.TOM.tom_mini.money.dto.TransactionDTO;
import com.TOM.tom_mini.money.entity.Account;
import com.TOM.tom_mini.money.entity.Transaction;
import com.TOM.tom_mini.money.entity.TransactionType;
import com.TOM.tom_mini.money.repository.AccountRepository;
import com.TOM.tom_mini.money.repository.TransactionRepository;
import com.TOM.tom_mini.money.service.MoneyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MoneyServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private MoneyService moneyService;

    private Account fromAccount;
    private Account toAccount;
    private Account vaultAccount;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        fromAccount = new Account();
        fromAccount.setAccountNo(1L);
        fromAccount.setBalance(BigDecimal.valueOf(100));

        toAccount = new Account();
        toAccount.setAccountNo(2L);
        toAccount.setBalance(BigDecimal.valueOf(50));

        vaultAccount = new Account();
        vaultAccount.setAccountNo(3L);
        vaultAccount.setAccountType("Vault");
        vaultAccount.setBalance(BigDecimal.ZERO);
    }

    @Test
    @Transactional
    public void testProcessTransactionTransferWithFee() {
        // Arrange
        BigDecimal transferAmount = BigDecimal.valueOf(10);
        BigDecimal fee = BigDecimal.valueOf(4);
        BigDecimal totalAmount = transferAmount.add(fee);

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setFromAccountNo(fromAccount.getAccountNo());
        transactionDTO.setToAccountNo(toAccount.getAccountNo());
        transactionDTO.setAmount(transferAmount);
        transactionDTO.setTransactionType(TransactionType.TRANSFER);
        transactionDTO.setDescription("Transfer with fee");

        when(accountRepository.findById(fromAccount.getAccountNo())).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById(toAccount.getAccountNo())).thenReturn(Optional.of(toAccount));
        when(accountRepository.findByAccountType("Vault")).thenReturn(Optional.of(vaultAccount));

        // Act
        Transaction transaction = moneyService.processTransaction(transactionDTO);

        // Assert
        assertEquals(0, fromAccount.getBalance().compareTo(BigDecimal.valueOf(86))); // 100 - 10 - 4
        assertEquals(0, toAccount.getBalance().compareTo(BigDecimal.valueOf(60))); // 50 + 10
        assertEquals(0, vaultAccount.getBalance().compareTo(BigDecimal.valueOf(4))); // 0 + 4
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(accountRepository, times(3)).save(any(Account.class));
    }


    @Test
    public void testProcessTransactionTransferWithInsufficientFunds() {
        // Arrange
        BigDecimal transferAmount = BigDecimal.valueOf(200);
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setFromAccountNo(fromAccount.getAccountNo());
        transactionDTO.setToAccountNo(toAccount.getAccountNo());
        transactionDTO.setAmount(transferAmount);
        transactionDTO.setTransactionType(TransactionType.TRANSFER);
        transactionDTO.setDescription("Transfer with insufficient funds");

        when(accountRepository.findById(fromAccount.getAccountNo())).thenReturn(Optional.of(fromAccount));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            moneyService.processTransaction(transactionDTO);
        });
    }

    @Test
    @Transactional
    public void testProcessTransactionDeposit() {
        // Arrange
        BigDecimal depositAmount = BigDecimal.valueOf(50);
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setFromAccountNo(fromAccount.getAccountNo());
        transactionDTO.setAmount(depositAmount);
        transactionDTO.setTransactionType(TransactionType.DEPOSIT);
        transactionDTO.setDescription("Deposit");

        when(accountRepository.findById(fromAccount.getAccountNo())).thenReturn(Optional.of(fromAccount));

        // Act
        Transaction transaction = moneyService.processTransaction(transactionDTO);

        // Assert
        assertEquals(fromAccount.getBalance(), BigDecimal.valueOf(150)); // 100 + 50
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    @Transactional
    public void testProcessTransactionWithdrawal() {
        // Arrange
        BigDecimal withdrawalAmount = BigDecimal.valueOf(30);
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setFromAccountNo(fromAccount.getAccountNo());
        transactionDTO.setAmount(withdrawalAmount);
        transactionDTO.setTransactionType(TransactionType.WITHDRAWAL);
        transactionDTO.setDescription("Withdrawal");

        when(accountRepository.findById(fromAccount.getAccountNo())).thenReturn(Optional.of(fromAccount));

        // Act
        Transaction transaction = moneyService.processTransaction(transactionDTO);

        // Assert
        assertEquals(fromAccount.getBalance(), BigDecimal.valueOf(70)); // 100 - 30
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    public void testProcessTransactionTransferToNonExistentAccount() {
        // Arrange
        BigDecimal transferAmount = BigDecimal.valueOf(10);
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setFromAccountNo(fromAccount.getAccountNo());
        transactionDTO.setToAccountNo(999L); // Non-existent account
        transactionDTO.setAmount(transferAmount);
        transactionDTO.setTransactionType(TransactionType.TRANSFER);
        transactionDTO.setDescription("Transfer to non-existent account");

        when(accountRepository.findById(fromAccount.getAccountNo())).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            moneyService.processTransaction(transactionDTO);
        });
    }
}

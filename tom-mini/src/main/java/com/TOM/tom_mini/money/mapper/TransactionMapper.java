package com.TOM.tom_mini.money.mapper;

import com.TOM.tom_mini.crm.other.IdGenerator;
import com.TOM.tom_mini.money.dto.TransactionDTO;
import com.TOM.tom_mini.money.entity.Transaction;
import com.TOM.tom_mini.money.request.TransactionCreateRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TransactionMapper {

    public Transaction transactionCreateRequestToTransaction(TransactionCreateRequest request){
        Transaction.TransactionBuilder transactionBuilder = Transaction.builder()
                .id(IdGenerator.generate())
                .transactionType(request.getTransactionType())
                .amount(request.getAmount())
                .description(request.getDescription())
                .transactionTime(LocalDate.now());

        return transactionBuilder.build();
    }

    public TransactionDTO transactionToTransactionDTO(Transaction transaction){
        TransactionDTO.TransactionDTOBuilder dtoBuilder = TransactionDTO.builder()
                .fromAccountNo(transaction.getFromAccount().getAccountNo())
                .description(transaction.getDescription())
                .amount(transaction.getAmount())
                .transactionDate(transaction.getTransactionTime())
                .transactionType(transaction.getTransactionType());

        if (transaction.getToAccount() != null) {
            dtoBuilder.toAccountNo(transaction.getToAccount().getAccountNo());
        }

        return dtoBuilder.build();
    }
}


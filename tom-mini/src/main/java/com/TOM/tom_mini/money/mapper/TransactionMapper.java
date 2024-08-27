package com.TOM.tom_mini.money.mapper;

import com.TOM.tom_mini.money.dto.TransactionDTO;
import com.TOM.tom_mini.money.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransactionMapper {

    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    @Mapping(source = "fromAccountNo", target = "fromAccount.accountNo")
    @Mapping(source = "toAccountNo", target = "toAccount.accountNo")
    @Mapping(source = "transactionDate", target = "transactionTime")
    Transaction toTransaction(TransactionDTO transactionDTO);

    @Mapping(source = "fromAccount.accountNo", target = "fromAccountNo")
    @Mapping(source = "toAccount.accountNo", target = "toAccountNo")
    @Mapping(source = "transactionTime", target = "transactionDate")
    TransactionDTO toTransactionDTO(Transaction transaction);
}

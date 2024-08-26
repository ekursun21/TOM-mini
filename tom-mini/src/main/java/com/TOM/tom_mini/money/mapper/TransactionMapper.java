package com.TOM.tom_mini.money.mapper;

import com.TOM.tom_mini.money.dto.TransactionDTO;
import com.TOM.tom_mini.money.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransactionMapper {

    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    @Mapping(target = "fromAccount", ignore = true) // We'll set these manually in the service
    @Mapping(target = "toAccount", ignore = true)   // We'll set these manually in the service
    Transaction toTransaction(TransactionDTO transactionDTO);

    @Mapping(source = "fromAccount.accountNo", target = "fromAccountNo")
    @Mapping(source = "toAccount.accountNo", target = "toAccountNo")
    TransactionDTO toTransactionDTO(Transaction transaction);
}

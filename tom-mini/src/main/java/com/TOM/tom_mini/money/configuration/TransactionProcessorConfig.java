package com.TOM.tom_mini.money.configuration;

import com.TOM.tom_mini.money.entity.TransactionType;
import com.TOM.tom_mini.money.service.processor.DepositProcessor;
import com.TOM.tom_mini.money.service.processor.TransactionProcessor;
import com.TOM.tom_mini.money.service.processor.TransferProcessor;
import com.TOM.tom_mini.money.service.processor.WithdrawalProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class TransactionProcessorConfig {

    @Bean
    public Map<TransactionType, TransactionProcessor> transactionProcessors(DepositProcessor depositProcessor,
                                                                            WithdrawalProcessor withdrawalProcessor,
                                                                            TransferProcessor transferProcessor) {
        return Map.of(
                TransactionType.DEPOSIT, depositProcessor,
                TransactionType.WITHDRAWAL, withdrawalProcessor,
                TransactionType.TRANSFER, transferProcessor
        );
    }
}

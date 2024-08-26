package com.TOM.tom_mini.money.service;

import com.TOM.tom_mini.money.repository.FeeRepository;
import com.TOM.tom_mini.money.entity.Fee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class FeeService {

    private final FeeRepository feeRepository;

    @Autowired
    public FeeService(FeeRepository feeRepository) {
        this.feeRepository = feeRepository;
    }

    @Cacheable(cacheNames = "fees", key = "#feeType + '_' + #accountType")
    public BigDecimal getFee(String feeType, String accountType) {
        return feeRepository.findByFeeTypeAndAccountType(feeType, accountType)
                .map(Fee::getAmount)
                .orElseThrow(() -> new IllegalArgumentException("Fee not found for type: " + feeType + " and account: " + accountType));
    }
}


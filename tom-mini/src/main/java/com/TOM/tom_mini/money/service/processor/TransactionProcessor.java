package com.TOM.tom_mini.money.service.processor;

import com.TOM.tom_mini.money.entity.Transaction;

public interface TransactionProcessor {
    void process(Transaction transaction);
}

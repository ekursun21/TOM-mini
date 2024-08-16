package com.TOM.tom_mini.money.repository;

import com.TOM.tom_mini.money.entity.Account;
import com.TOM.tom_mini.money.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByFromAccountAndTransactionTimeBetween(Account fromAccount, LocalDate startDate, LocalDate endDate);
}



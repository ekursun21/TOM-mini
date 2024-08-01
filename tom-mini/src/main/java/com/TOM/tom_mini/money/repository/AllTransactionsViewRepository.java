package com.TOM.tom_mini.money.repository;

import com.TOM.tom_mini.money.dto.MonthlyTransactionSummaryDTO;
import com.TOM.tom_mini.money.entity.AllTransactionsView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface AllTransactionsViewRepository extends JpaRepository<AllTransactionsView, UUID> {
    List<AllTransactionsView> findByAccountNo(String accountNo);

    @Query("SELECT new com.tom.tom_mini.money.dto.MonthlyTransactionSummaryDTO(FUNCTION('YEAR', t.transactionDate) as year, FUNCTION('MONTH', t.transactionDate) as month, SUM(t.amount) as totalAmount, COUNT(t) as transactionCount) " +
            "FROM AllTransactionsView t " +
            "GROUP BY FUNCTION('YEAR', t.transactionDate), FUNCTION('MONTH', t.transactionDate)")
    List<MonthlyTransactionSummaryDTO> findMonthlyTransactionSummaries();
}

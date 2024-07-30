package com.TOM.tom_mini.money.repository;

import com.TOM.tom_mini.money.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    boolean existsByAccountNo(String accountNo);

    Optional<Account> findByAccountNo(String accountNo);
}

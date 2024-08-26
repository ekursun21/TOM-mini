package com.TOM.tom_mini.money.repository;

import com.TOM.tom_mini.money.entity.Fee;
import net.bytebuddy.jar.asm.commons.Remapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeeRepository extends JpaRepository<Fee, Long> {
    Optional<Fee> findByFeeTypeAndAccountType(String feeType, String accountType);
}


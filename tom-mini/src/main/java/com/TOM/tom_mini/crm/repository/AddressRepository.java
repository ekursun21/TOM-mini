package com.TOM.tom_mini.crm.repository;

import com.TOM.tom_mini.crm.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
}

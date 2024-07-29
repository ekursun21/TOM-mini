package com.TOM.tom_mini.crm.repository;

import com.TOM.tom_mini.crm.entity.CustomerAddress;
import com.TOM.tom_mini.crm.entity.CustomerAddressId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, CustomerAddressId> {
}

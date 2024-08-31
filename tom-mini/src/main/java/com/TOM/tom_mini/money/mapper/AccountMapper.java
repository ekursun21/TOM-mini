package com.TOM.tom_mini.money.mapper;

import com.TOM.tom_mini.crm.other.IdGenerator;
import com.TOM.tom_mini.crm.service.CustomerService;
import com.TOM.tom_mini.money.dto.AccountDTO;
import com.TOM.tom_mini.money.entity.Account;
import com.TOM.tom_mini.money.request.AccountCreateRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AccountMapper {

    private final CustomerService customerService;

    public AccountMapper(CustomerService customerService) {
        this.customerService = customerService;
    }

    public AccountDTO accountToAccountDto(Account account) {
        return AccountDTO.builder()
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .customerId(account.getCustomer().getId())
                .createdAt(account.getCreatedAt())
                .build();
    }

    public Account accountCreateRequestToAccount(AccountCreateRequest request) {
        return Account.builder()
                .accountNo(IdGenerator.generate())
                .accountType(request.getAccountType())
                .balance(request.getBalance())
                .createdAt(LocalDate.now())
                .customer(customerService.getCustomerById(request.getCustomerId()).get())
                .build();
    }
}

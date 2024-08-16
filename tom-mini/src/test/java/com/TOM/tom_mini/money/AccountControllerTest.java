package com.TOM.tom_mini.money;

import com.TOM.tom_mini.money.controller.AccountController;
import com.TOM.tom_mini.money.dto.TransactionDTO;
import com.TOM.tom_mini.money.entity.TransactionType;
import com.TOM.tom_mini.money.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    private List<TransactionDTO> transactions;

    @BeforeEach
    void setUp() {
        TransactionDTO transaction1 = new TransactionDTO(
                123L,
                456L,
                TransactionType.TRANSFER,
                new BigDecimal("500.00"),
                "Payment for services",
                LocalDate.of(2024, 8, 1)
        );

        TransactionDTO transaction2 = new TransactionDTO(
                123L,
                789L,
                TransactionType.DEPOSIT,
                new BigDecimal("200.00"),
                "Salary deposit",
                LocalDate.of(2024, 8, 15)
        );

        transactions = Arrays.asList(transaction1, transaction2);

        Mockito.when(accountService.getMonthlyTransactions(anyLong(), anyInt(), anyInt()))
                .thenReturn(transactions);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetMonthlyTransactions() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/accounts/123/monthly-transactions")
                        .param("year", "2024")
                        .param("month", "8")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].fromAccountNo", is(123)))
                .andExpect(jsonPath("$[0].toAccountNo", is(456)))
                .andExpect(jsonPath("$[0].amount", is(500.00)))
                .andExpect(jsonPath("$[0].transactionType", is("TRANSFER")))
                .andExpect(jsonPath("$[1].fromAccountNo", is(123)))
                .andExpect(jsonPath("$[1].toAccountNo", is(789)))
                .andExpect(jsonPath("$[1].amount", is(200.00)))
                .andExpect(jsonPath("$[1].transactionType", is("DEPOSIT")));
    }
}

package com.TOM.tom_mini.AuthenticationTest;

import com.TOM.tom_mini.AuthenticationTest.TestSecurityConfig;
import com.TOM.tom_mini.crm.controller.CustomerController;
import com.TOM.tom_mini.crm.service.CustomerService;
import com.TOM.tom_mini.money.controller.AccountController;
import com.TOM.tom_mini.money.controller.MoneyController;
import com.TOM.tom_mini.money.service.AccountService;
import com.TOM.tom_mini.money.service.MoneyService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {CustomerController.class, MoneyController.class, AccountController.class})
@Import(TestSecurityConfig.class)  // Import your security configuration
public class AuthenticationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;
    @MockBean
    private AccountService accountService;
    @MockBean
    private MoneyService moneyService;  // Mock the MoneyService used by MoneyController

    @Test
    @WithMockUser(username="john.doe@example.com", roles={"USER"})
    public void testAuthenticationSuccess() throws Exception {
        mockMvc.perform(get("/api/customer/private"))  // Update with actual endpoint
                .andExpect(status().isOk());
    }

    @Test
    public void testAuthenticationFailure() throws Exception {
        mockMvc.perform(get("/some-secured-url")  // Update with actual endpoint
                        .with(httpBasic("user@example.com", "wrongpassword")))
                .andExpect(status().isUnauthorized());
    }
}

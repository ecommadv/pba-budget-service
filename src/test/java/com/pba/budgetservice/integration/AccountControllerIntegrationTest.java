package com.pba.budgetservice.integration;

import com.PBA.budgetservice.controller.advice.ApiExceptionResponse;
import com.PBA.budgetservice.controller.request.AccountCreateRequest;
import com.PBA.budgetservice.exceptions.ErrorCodes;
import com.PBA.budgetservice.gateway.UserGateway;
import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.dtos.AccountDto;
import com.PBA.budgetservice.persistance.repository.AccountDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import mockgenerators.AccountMockGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AccountControllerIntegrationTest extends BaseControllerIntegrationTest {
    @Autowired
    private AccountDao accountDao;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserGateway userGateway;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void testCreateAccount() throws Exception {
        // given
        AccountCreateRequest accountCreateRequest = AccountMockGenerator.generateMockAccountCreateRequest();
        String accountCreateRequestJSON = objectMapper.writeValueAsString(accountCreateRequest);
        String authHeader = "Bearer token";
        UUID userUid = UUID.randomUUID();
        when(userGateway.getUserUidFromAuthHeader(authHeader)).thenReturn(userUid);

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountCreateRequestJSON)
                        .header("Authorization", authHeader))
                .andExpect(status().isCreated())
                .andReturn();
        String accountDtoJSON = result.getResponse().getContentAsString();
        AccountDto accountDto = objectMapper.readValue(accountDtoJSON, AccountDto.class);

        // then
        assertEquals(userUid, accountDto.getUserUid());
        assertEquals(accountCreateRequest.getCurrency(), accountDto.getCurrency());
        assertEquals(accountDao.getAll().size(), 1);
        assertTrue(accountDao.getByUserUidAndCurrency(Pair.of(userUid, accountDto.getCurrency())).isPresent());
    }

    @Test
    public void testCreateAccountWhichAlreadyExists() throws Exception {
        // given
        AccountCreateRequest accountCreateRequest = AccountMockGenerator.generateMockAccountCreateRequest();
        String accountCreateRequestJSON = objectMapper.writeValueAsString(accountCreateRequest);
        String authHeader = "Bearer token";
        UUID userUid = UUID.randomUUID();
        when(userGateway.getUserUidFromAuthHeader(authHeader)).thenReturn(userUid);
        this.saveAccountWithCurrencyAndUserUid(accountCreateRequest.getCurrency(), userUid);

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountCreateRequestJSON)
                        .header("Authorization", authHeader))
                .andExpect(status().isBadRequest())
                .andReturn();
        String apiExceptionResponseJSON = result.getResponse().getContentAsString();
        ApiExceptionResponse apiExceptionResponse = objectMapper.readValue(apiExceptionResponseJSON, ApiExceptionResponse.class);

        // then
        Map<String, String> expectedErrors = Map.of(
                ErrorCodes.ACCOUNT_ALREADY_EXISTS,
                String.format("Account with user uid %s and currency %s already exists",
                        userUid,
                        accountCreateRequest.getCurrency())
        );
        assertEquals(expectedErrors, apiExceptionResponse.errors());
    }

    private void saveAccountWithCurrencyAndUserUid(String currency, UUID userUid) {
        Account account = AccountMockGenerator.generateMockAccount(currency, userUid);
        accountDao.save(account);
    }
}
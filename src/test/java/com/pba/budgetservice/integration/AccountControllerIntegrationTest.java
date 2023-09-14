package com.pba.budgetservice.integration;

import com.PBA.budgetservice.controller.advice.ApiExceptionResponse;
import com.PBA.budgetservice.controller.request.AccountCreateRequest;
import com.PBA.budgetservice.exceptions.ErrorCodes;
import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.dtos.AccountDto;
import com.PBA.budgetservice.persistance.repository.AccountDao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import mockgenerators.AccountMockGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountControllerIntegrationTest extends BaseControllerIntegrationTest {
    @Autowired
    private AccountDao accountDao;

    @Autowired
    private MockMvc mockMvc;

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
        String createAccountEndpoint = "/account";
        String accountCreateRequestJSON = objectMapper.writeValueAsString(accountCreateRequest);

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(createAccountEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountCreateRequestJSON))
                .andExpect(status().isCreated())
                .andReturn();
        String accountDtoJSON = result.getResponse().getContentAsString();
        AccountDto accountDto = objectMapper.readValue(accountDtoJSON, AccountDto.class);

        // then
        assertEquals(1, accountDao.getAll().size());
        assertTrue(accountDao.getByUserUidAndCurrency(Pair.of(accountCreateRequest.getUserUid(), accountCreateRequest.getCurrency())).isPresent());
        assertEquals(accountDto.getUserUid(), accountCreateRequest.getUserUid());
        assertEquals(accountDto.getCurrency(), accountCreateRequest.getCurrency());
    }

    @Test
    public void testCreateAccountThatAlreadyExists() throws Exception {
        // given
        Account existentAccount = AccountMockGenerator.generateMockAccount();
        accountDao.save(existentAccount);
        AccountCreateRequest accountCreateRequest = AccountCreateRequest.builder()
                .userUid(existentAccount.getUserUid())
                .currency(existentAccount.getCurrency())
                .build();
        String createAccountEndpoint = "/account";
        String accountCreateRequestJSON = objectMapper.writeValueAsString(accountCreateRequest);

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(createAccountEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountCreateRequestJSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        String responseJSON = result.getResponse().getContentAsString();
        ApiExceptionResponse response = objectMapper.readValue(responseJSON, ApiExceptionResponse.class);

        // then
        Map<String, String> expectedErrors = Map.of(
                ErrorCodes.ACCOUNT_ALREADY_EXISTS,
                String.format("Account with user uid %s and currency %s already exists", existentAccount.getUserUid(), existentAccount.getCurrency())
        );
        assertEquals(expectedErrors, response.errors());
    }
}
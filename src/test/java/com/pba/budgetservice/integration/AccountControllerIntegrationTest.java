package com.pba.budgetservice.integration;

import com.PBA.budgetservice.controller.request.AccountCreateRequest;
import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.dtos.AccountDto;
import com.PBA.budgetservice.persistance.repository.AccountDao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mockgenerators.AccountMockGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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
}

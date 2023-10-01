package com.pba.budgetservice.integration;

import com.PBA.budgetservice.controller.advice.ApiExceptionResponse;
import com.PBA.budgetservice.controller.request.AccountCreateRequest;
import com.PBA.budgetservice.exceptions.ErrorCodes;
import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.dtos.AccountDto;
import com.PBA.budgetservice.persistance.repository.AccountDao;
import com.PBA.budgetservice.persistance.repository.CurrencyRateDao;
import com.PBA.budgetservice.scheduler.CurrencyScheduler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.pba.budgetservice.mockgenerators.AccountMockGenerator;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

import static com.pba.budgetservice.mockgenerators.MockToken.MOCK_USER_ACCESS_TOKEN;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

@ActiveProfiles({"default", "test"})
@WireMockTest(httpPort = 8089)
public class AccountControllerIntegrationTest extends BaseControllerIntegrationTest {
    @Autowired
    private AccountDao accountDao;

    @Autowired
    private CurrencyRateDao currencyRateDao;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CurrencyScheduler currencyScheduler;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @BeforeEach
    public void reloadCurrencyRates() {
        currencyScheduler.reloadCurrencyRates();
    }

    @Test
    public void testCreateAccount() throws Exception {
        // given
        AccountCreateRequest accountCreateRequest = AccountMockGenerator.generateMockAccountCreateRequest(currencyRateDao.getAll());
        String accountCreateRequestJSON = objectMapper.writeValueAsString(accountCreateRequest);
        String authHeader = String.format("Bearer %s", MOCK_USER_ACCESS_TOKEN);
        UUID userUid = UUID.randomUUID();
        this.stubUserDtoResponse(userUid);

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
        AccountCreateRequest accountCreateRequest = AccountMockGenerator.generateMockAccountCreateRequest(currencyRateDao.getAll());
        String accountCreateRequestJSON = objectMapper.writeValueAsString(accountCreateRequest);
        String authHeader = String.format("Bearer %s", MOCK_USER_ACCESS_TOKEN);
        UUID userUid = UUID.randomUUID();
        this.stubUserDtoResponse(userUid);
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

    @Test
    public void testCreateAccountWithNonexistentCurrency() throws Exception {
        // given
        AccountCreateRequest accountCreateRequest = AccountMockGenerator.generateMockAccountCreateRequest();
        String accountCreateRequestJSON = objectMapper.writeValueAsString(accountCreateRequest);
        String authHeader = String.format("Bearer %s", MOCK_USER_ACCESS_TOKEN);
        UUID userUid = UUID.randomUUID();
        this.stubUserDtoResponse(userUid);

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountCreateRequestJSON)
                        .header("Authorization", authHeader))
                .andExpect(status().isNotFound())
                .andReturn();
        String apiExceptionResponseJSON = result.getResponse().getContentAsString();
        ApiExceptionResponse apiExceptionResponse = objectMapper.readValue(apiExceptionResponseJSON, ApiExceptionResponse.class);

        // then
        Map<String, String> expectedErrors = Map.of(
                ErrorCodes.CURRENCY_RATE_NOT_FOUND,
                String.format("Currency rate with code %s does not exist",
                        accountCreateRequest.getCurrency())
        );
        assertEquals(expectedErrors, apiExceptionResponse.errors());
    }

    private void saveAccountWithCurrencyAndUserUid(String currency, UUID userUid) {
        Account account = AccountMockGenerator.generateMockAccount(currency, userUid);
        accountDao.save(account);
    }

    private void stubUserDtoResponse(UUID userUid) throws IOException {
        String mockUserResponseJSON = String.format(getFileContent("classpath:mock_user_response.json"), userUid);
        stubFor(get(urlEqualTo("/api/user"))
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBody(mockUserResponseJSON)));
    }

    private String getFileContent(String path) throws IOException {
        return FileUtils.readFileToString(ResourceUtils.getFile(path), StandardCharsets.UTF_8);
    }
}
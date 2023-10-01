package com.pba.budgetservice.integration;

import com.PBA.budgetservice.controller.advice.ApiExceptionResponse;
import com.PBA.budgetservice.controller.request.IncomeUpdateRequest;
import com.PBA.budgetservice.exceptions.ErrorCodes;
import com.PBA.budgetservice.gateway.UserGateway;
import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.Income;
import com.PBA.budgetservice.persistance.model.IncomeCategory;
import com.PBA.budgetservice.persistance.model.dtos.IncomeCategoryDto;
import com.PBA.budgetservice.persistance.model.dtos.IncomeDto;
import com.PBA.budgetservice.controller.request.IncomeCreateRequest;
import com.PBA.budgetservice.persistance.repository.AccountDao;
import com.PBA.budgetservice.persistance.repository.CurrencyRateDao;
import com.PBA.budgetservice.persistance.repository.IncomeCategoryDao;
import com.PBA.budgetservice.persistance.repository.IncomeDao;
import com.PBA.budgetservice.scheduler.CurrencyScheduler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.pba.budgetservice.mockgenerators.AccountMockGenerator;
import com.pba.budgetservice.mockgenerators.IncomeMockGenerator;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.ResourceUtils;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.pba.budgetservice.mockgenerators.MockToken.MOCK_USER_ACCESS_TOKEN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@ActiveProfiles({"default", "test"})
@WireMockTest(httpPort = 8089)
public class IncomeControllerIntegrationTest extends BaseControllerIntegrationTest {
    @Autowired
    private IncomeDao incomeDao;

    @Autowired
    private IncomeCategoryDao incomeCategoryDao;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private CurrencyRateDao currencyRateDao;

    @Autowired
    private CurrencyScheduler currencyScheduler;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @BeforeEach
    public void reloadAllCurrencyRates() {
        currencyScheduler.reloadCurrencyRates();
    }

    @Test
    public void testCreateIncome() throws Exception {
        // given
        List<IncomeCategory> incomeCategoryList = incomeCategoryDao.getAll();
        IncomeCreateRequest incomeRequest = IncomeMockGenerator.generateMockIncomeCreateRequest(incomeCategoryList, currencyRateDao.getAll());
        String incomeRequestJSON = objectMapper.writeValueAsString(incomeRequest);
        String authHeader = String.format("Bearer %s", MOCK_USER_ACCESS_TOKEN);
        UUID userUid = UUID.randomUUID();
        this.stubUserDtoResponse(userUid);

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/income")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(incomeRequestJSON)
                        .header("Authorization", authHeader))
                .andExpect(status().isCreated())
                .andReturn();
        String incomeDtoJSON = result.getResponse().getContentAsString();
        IncomeDto incomeDto = objectMapper.readValue(incomeDtoJSON, IncomeDto.class);

        // then
        Assertions.assertEquals(1, incomeDao.getAll().size());
        Assertions.assertTrue(accountDao.getByUserUidAndCurrency(Pair.of(userUid, incomeRequest.getCurrency())).isPresent());
        Assertions.assertEquals(incomeDto.getAmount(), incomeDao.getByUid(incomeDto.getUid()).get().getAmount());
    }

    @Test
    public void testGetAllIncomesByUserUidAndCurrency() throws Exception {
        // given
        List<IncomeCategory> incomeCategoryList = incomeCategoryDao.getAll();
        List<Account> accounts = AccountMockGenerator.generateMockListOfAccounts(5);
        this.addMockAccounts(accounts);
        List<Income> incomes = IncomeMockGenerator.generateMockListOfIncomes(incomeCategoryList, accountDao.getAll(), 10);
        Account accountToSearchBy = accountDao.getAll().stream().findFirst().get();
        Long accountId = accountToSearchBy.getId();
        String currency = accountToSearchBy.getCurrency();
        UUID userUid = accountToSearchBy.getUserUid();
        incomes.forEach(income -> {
            income.setAccountId(accountId);
            income.setCurrency(currency);
        });
        this.addMockIncomes(incomes);
        String authHeader = String.format("Bearer %s", MOCK_USER_ACCESS_TOKEN);
        this.stubUserDtoResponse(userUid);
        String getEndpoint = String.format("/income/currency?currency=%s", currency);

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(getEndpoint)
                        .header("Authorization", authHeader))
                .andExpect(status().isOk())
                .andReturn();
        String incomeDtosJSON = result.getResponse().getContentAsString();
        List<IncomeDto> incomeDtos = objectMapper.readValue(incomeDtosJSON, new TypeReference<>() {});

        // then
        Assertions.assertEquals(incomes.size(), incomeDtos.size());
        List<UUID> incomesUids = incomes.stream().map(Income::getUid).toList();
        List<UUID> incomeDtosUids = incomeDtos.stream().map(IncomeDto::getUid).toList();
        Assertions.assertEquals(incomesUids, incomeDtosUids);
    }

    @Test
    public void testGetAllIncomesByNonexistentUserUidAndCurrency() throws Exception {
        // given
        UUID nonexistentUid = UUID.randomUUID();
        String nonexistentCurrency = "ABC";
        String getEndpoint = String.format("/income/currency?&currency=%s", nonexistentCurrency);
        String authHeader = String.format("Bearer %s", MOCK_USER_ACCESS_TOKEN);
        this.stubUserDtoResponse(nonexistentUid);

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(getEndpoint)
                        .header("Authorization", authHeader))
                .andExpect(status().isNotFound())
                .andReturn();
        String responseJSON = result.getResponse().getContentAsString();
        ApiExceptionResponse response = objectMapper.readValue(responseJSON, ApiExceptionResponse.class);

        // then
        Map<String, String> expectedErrors = Map.of(
                ErrorCodes.ACCOUNT_NOT_FOUND,
                String.format("Account with user uid %s and currency %s does not exist", nonexistentUid, nonexistentCurrency)
        );
        assertEquals(expectedErrors, response.errors());
    }

    @Test
    public void testUpdateIncome() throws Exception {
        // given
        List<IncomeCategory> incomeCategoryList = incomeCategoryDao.getAll();
        List<Account> accounts = AccountMockGenerator.generateMockListOfAccounts(5);
        this.addMockAccounts(accounts);
        Income income = IncomeMockGenerator.generateMockIncome(incomeCategoryList, accountDao.getAll());
        Income savedIncome = incomeDao.save(income);
        IncomeUpdateRequest incomeUpdateRequest = IncomeMockGenerator.generateMockIncomeUpdateRequest(incomeCategoryList);
        IncomeCategory incomeUpdateRequestCategory = incomeCategoryDao.getIncomeCategoryByUid(incomeUpdateRequest.getCategoryUid()).get();
        String incomeUpdateRequestJSON = objectMapper.writeValueAsString(incomeUpdateRequest);

        Account account = accountDao.getById(savedIncome.getAccountId()).get();
        String authHeader = String.format("Bearer %s", MOCK_USER_ACCESS_TOKEN);
        this.stubUserDtoResponse(account.getUserUid());
        String updateEndpoint = String.format("/income/%s", income.getUid().toString());

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(updateEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(incomeUpdateRequestJSON)
                        .header("Authorization", authHeader))
                .andExpect(status().isOk())
                .andReturn();
        String incomeDtoJSON = result.getResponse().getContentAsString();
        IncomeDto incomeDto = objectMapper.readValue(incomeDtoJSON, IncomeDto.class);

        // then
        Income updatedIncome = incomeDao.getById(savedIncome.getId()).get();
        Assertions.assertEquals(incomeDto.getUid(), income.getUid());
        Assertions.assertNotEquals(savedIncome, incomeDao.getByUid(incomeDto.getUid()).get());
        Assertions.assertEquals(incomeUpdateRequest.getAmount(), updatedIncome.getAmount());
        Assertions.assertEquals(incomeUpdateRequest.getDescription(), updatedIncome.getDescription());
        Assertions.assertEquals(incomeUpdateRequestCategory.getId(), updatedIncome.getCategoryId());
    }

    @Test
    public void testDeleteIncome() throws Exception {
        // given
        List<IncomeCategory> incomeCategoryList = incomeCategoryDao.getAll();
        List<Account> accounts = AccountMockGenerator.generateMockListOfAccounts(5);
        this.addMockAccounts(accounts);
        Income income = IncomeMockGenerator.generateMockIncome(incomeCategoryList, accountDao.getAll());
        Income savedIncome = incomeDao.save(income);

        Account account = accountDao.getById(savedIncome.getAccountId()).get();
        String authHeader = String.format("Bearer %s", MOCK_USER_ACCESS_TOKEN);
        this.stubUserDtoResponse(account.getUserUid());
        String deleteEndpoint = String.format("/income/%s", savedIncome.getUid().toString());

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete(deleteEndpoint)
                        .header("Authorization", authHeader))
                .andExpect(status().isOk())
                .andReturn();

        // then
        Assertions.assertEquals(0, incomeDao.getAll().size());
        Assertions.assertFalse(incomeDao.getById(savedIncome.getId()).isPresent());
    }

    @Test
    public void testGetAllIncomeCategories() throws Exception {
        List<IncomeCategory> incomeCategoryList = incomeCategoryDao.getAll();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/income/category"))
                .andExpect(status().isOk())
                .andReturn();
        String incomeCategoryDtosJSON = result.getResponse().getContentAsString();
        List<IncomeCategoryDto> incomeCategoryDtos = objectMapper.readValue(incomeCategoryDtosJSON, new TypeReference<List<IncomeCategoryDto>>(){});
        List<UUID> expectedUids = incomeCategoryList.stream().map(IncomeCategory::getUid).toList();
        List<UUID> resultedUids = incomeCategoryDtos.stream().map(IncomeCategoryDto::getUid).toList();

        Assertions.assertEquals(expectedUids, resultedUids);
    }

    private void addMockAccounts(List<Account> accounts) {
        accounts.forEach(account -> accountDao.save(account));
    }

    private void addMockIncomes(List<Income> incomes) {
        incomes.forEach(income -> incomeDao.save(income));
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
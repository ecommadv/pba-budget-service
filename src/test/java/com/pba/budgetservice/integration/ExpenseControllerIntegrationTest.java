package com.pba.budgetservice.integration;

import com.PBA.budgetservice.controller.advice.ApiExceptionResponse;
import com.PBA.budgetservice.controller.request.ExpenseCreateRequest;
import com.PBA.budgetservice.controller.request.ExpenseUpdateRequest;
import com.PBA.budgetservice.exceptions.ErrorCodes;
import com.PBA.budgetservice.persistance.model.*;
import com.PBA.budgetservice.persistance.model.dtos.ExpenseCategoryDto;
import com.PBA.budgetservice.persistance.model.dtos.ExpenseDto;
import com.PBA.budgetservice.persistance.repository.AccountDao;
import com.PBA.budgetservice.persistance.repository.CurrencyRateDao;
import com.PBA.budgetservice.persistance.repository.ExpenseCategoryDao;
import com.PBA.budgetservice.persistance.repository.ExpenseDao;
import com.PBA.budgetservice.scheduler.CurrencyScheduler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.pba.budgetservice.mockgenerators.AccountMockGenerator;
import com.pba.budgetservice.mockgenerators.ExpenseMockGenerator;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles({"default", "test"})
@WireMockTest(httpPort = 8089)
public class ExpenseControllerIntegrationTest extends BaseControllerIntegrationTest {
    @Autowired
    private ExpenseDao expenseDao;

    @Autowired
    private ExpenseCategoryDao expenseCategoryDao;

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
    public void reloadCurrencyRates() {
        currencyScheduler.reloadCurrencyRates();
    }

    @Test
    public void testCreateExpense() throws Exception {
        // given
        List<ExpenseCategory> expenseCategories = expenseCategoryDao.getAll();
        List<Account> accounts = AccountMockGenerator.generateMockListOfAccounts(5);
        this.addMockAccounts(accounts);
        ExpenseCreateRequest expenseCreateRequest = ExpenseMockGenerator.generateMockExpenseCreateRequest(expenseCategories, currencyRateDao.getAll());
        String expenseRequestJSON = objectMapper.writeValueAsString(expenseCreateRequest);
        String authHeader = "Bearer token";
        UUID userUid = UUID.randomUUID();
        this.stubUserDtoResponse(userUid);

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/expense")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expenseRequestJSON)
                        .header("Authorization", authHeader))
                .andExpect(status().isCreated())
                .andReturn();
        String expenseDtoJSON = result.getResponse().getContentAsString();
        ExpenseDto expenseDto = objectMapper.readValue(expenseDtoJSON, ExpenseDto.class);

        // then
        Assertions.assertEquals(1, expenseDao.getAll().size());
        Assertions.assertTrue(accountDao.getByUserUidAndCurrency(Pair.of(userUid, expenseCreateRequest.getCurrency())).isPresent());
        Assertions.assertEquals(expenseDto.getAmount(), expenseDao.getByUid(expenseDto.getUid()).get().getAmount());
    }

    @Test
    public void testGetAllExpensesByUserUidAndCurrency() throws Exception {
        // given
        List<ExpenseCategory> expenseCategories = expenseCategoryDao.getAll();
        List<Account> accounts = AccountMockGenerator.generateMockListOfAccounts(5);
        this.addMockAccounts(accounts);
        List<Expense> expenses = ExpenseMockGenerator.generateMockListOfExpenses(expenseCategories, accountDao.getAll(), currencyRateDao.getAll(), 10);
        Account accountToSearchBy = accountDao.getAll().stream().findFirst().get();
        Long accountId = accountToSearchBy.getId();
        String currency = accountToSearchBy.getCurrency();
        UUID userUid = accountToSearchBy.getUserUid();
        expenses.forEach((expense) -> {
            expense.setAccountId(accountId);
            expense.setCurrency(currency);
        });
        this.addMockExpenses(expenses);

        String authHeader = "Bearer token";
        this.stubUserDtoResponse(userUid);
        String getEndpoint = String.format("/expense?userUid=%s&currency=%s", userUid.toString(), currency);

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(getEndpoint)
                        .header("Authorization", authHeader))
                .andExpect(status().isOk())
                .andReturn();
        String expenseDtosJSON = result.getResponse().getContentAsString();
        List<ExpenseDto> expenseDtos = objectMapper.readValue(expenseDtosJSON, new TypeReference<List<ExpenseDto>>(){});

        // then
        Assertions.assertEquals(expenseDtos.size(), expenseDtos.size());
        List<UUID> expectedUids = expenses.stream().map(Expense::getUid).toList();
        List<UUID> resultedUids = expenseDtos.stream().map(ExpenseDto::getUid).toList();
        Assertions.assertEquals(expectedUids, resultedUids);
    }

    @Test
    public void testGetAllExpensesByNonexistentUserUidAndCurrency() throws Exception {
        // given
        UUID nonexistentUid = UUID.randomUUID();
        String nonexistentCurrency = "RON";
        String getEndpoint = String.format("/expense?userUid=%s&currency=%s", nonexistentUid, nonexistentCurrency);

        String authHeader = "Bearer token";
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
    public void testUpdateExpense() throws Exception {
        // given
        List<ExpenseCategory> expenseCategories = expenseCategoryDao.getAll();
        List<Account> accounts = AccountMockGenerator.generateMockListOfAccounts(5);
        this.addMockAccounts(accounts);
        Expense expense = ExpenseMockGenerator.generateMockExpense(expenseCategories, accountDao.getAll(), currencyRateDao.getAll());
        Expense savedExpense = expenseDao.save(expense);
        ExpenseUpdateRequest expenseUpdateRequest = ExpenseMockGenerator.generateMockExpenseUpdateRequest(expenseCategories);
        ExpenseCategory expenseUpdateRequestCategory = expenseCategoryDao.getByUid(expenseUpdateRequest.getCategoryUid()).get();
        String expenseUpdateRequestJSON = objectMapper.writeValueAsString(expenseUpdateRequest);
        String authHeader = "Bearer token";
        Account account = accountDao.getById(expense.getAccountId()).get();
        this.stubUserDtoResponse(account.getUserUid());

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(String.format("/expense/%s", expense.getUid().toString()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expenseUpdateRequestJSON)
                        .header("Authorization", authHeader))
                .andExpect(status().isOk())
                .andReturn();
        String expenseDtoJSON = result.getResponse().getContentAsString();
        ExpenseDto expenseDto = objectMapper.readValue(expenseDtoJSON, ExpenseDto.class);

        // then
        Expense updatedExpense = expenseDao.getById(savedExpense.getId()).get();
        Assertions.assertEquals(expenseDto.getUid(), expense.getUid());
        Assertions.assertNotEquals(savedExpense, expenseDao.getByUid(expenseDto.getUid()).get());
        Assertions.assertEquals(expenseUpdateRequest.getAmount(), updatedExpense.getAmount());
        Assertions.assertEquals(expenseUpdateRequest.getName(), updatedExpense.getName());
        Assertions.assertEquals(expenseUpdateRequest.getDescription(), updatedExpense.getDescription());
        Assertions.assertEquals(expenseUpdateRequestCategory.getId(), updatedExpense.getCategoryId());
    }

    @Test
    public void testDeleteExpense() throws Exception {
        // given
        List<ExpenseCategory> expenseCategories = expenseCategoryDao.getAll();
        List<Account> accounts = AccountMockGenerator.generateMockListOfAccounts(5);
        this.addMockAccounts(accounts);
        Expense expense = ExpenseMockGenerator.generateMockExpense(expenseCategories, accountDao.getAll(), currencyRateDao.getAll());
        Expense savedExpense = expenseDao.save(expense);
        String authHeader = "Bearer token";
        UUID userUid = accountDao.getById(savedExpense.getAccountId()).get().getUserUid();
        this.stubUserDtoResponse(userUid);
        String deleteEndpoint = String.format("/expense/%s", savedExpense.getUid().toString());

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete(deleteEndpoint)
                        .header("Authorization", authHeader))
                .andExpect(status().isOk())
                .andReturn();

        // then
        Assertions.assertEquals(0, expenseDao.getAll().size());
        Assertions.assertFalse(expenseDao.getById(savedExpense.getId()).isPresent());
    }

    @Test
    public void testGetAllExpenseCategories() throws Exception {
        List<ExpenseCategory> expenseCategories = expenseCategoryDao.getAll();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/expense/category"))
                .andExpect(status().isOk())
                .andReturn();
        String expenseCategoryDtosJSON = result.getResponse().getContentAsString();
        List<ExpenseCategoryDto> expenseCategoryDtos = objectMapper.readValue(expenseCategoryDtosJSON, new TypeReference<List<ExpenseCategoryDto>>(){});
        List<UUID> expectedUids = expenseCategories.stream().map(ExpenseCategory::getUid).toList();
        List<UUID> resultedUids = expenseCategoryDtos.stream().map(ExpenseCategoryDto::getUid).toList();

        Assertions.assertEquals(expectedUids, resultedUids);
    }

    private void addMockAccounts(List<Account> accounts) {
        accounts.forEach((account) -> accountDao.save(account));
    }

    private void addMockExpenses(List<Expense> expenses) {
        expenses.forEach(expense -> expenseDao.save(expense));
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

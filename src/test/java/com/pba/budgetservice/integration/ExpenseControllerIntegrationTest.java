package com.pba.budgetservice.integration;

import com.PBA.budgetservice.controller.request.ExpenseCreateRequest;
import com.PBA.budgetservice.controller.request.ExpenseUpdateRequest;
import com.PBA.budgetservice.persistance.model.*;
import com.PBA.budgetservice.persistance.model.dtos.ExpenseDto;
import com.PBA.budgetservice.persistance.repository.AccountDao;
import com.PBA.budgetservice.persistance.repository.ExpenseCategoryDao;
import com.PBA.budgetservice.persistance.repository.ExpenseDao;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import mockgenerators.AccountMockGenerator;
import mockgenerators.ExpenseMockGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ExpenseControllerIntegrationTest extends BaseControllerIntegrationTest {
    @Autowired
    private ExpenseDao expenseDao;

    @Autowired
    private ExpenseCategoryDao expenseCategoryDao;

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
    public void testCreateExpense() throws Exception {
        List<ExpenseCategory> expenseCategories = expenseCategoryDao.getAll();
        List<Account> accounts = AccountMockGenerator.generateMockListOfAccounts(5);
        this.addMockAccounts(accounts);
        ExpenseCreateRequest expenseCreateRequest = ExpenseMockGenerator.generateMockExpenseCreateRequest(expenseCategories, accountDao.getAll());
        String expenseRequestJSON = objectMapper.writeValueAsString(expenseCreateRequest);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/expense")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expenseRequestJSON)
                ).andExpect(status().isCreated())
                .andReturn();
        String expenseDtoJSON = result.getResponse().getContentAsString();
        ExpenseDto expenseDto = objectMapper.readValue(expenseDtoJSON, ExpenseDto.class);

        Assertions.assertEquals(1, expenseDao.getAll().size());
        Assertions.assertTrue(accountDao.getByUserUidAndCurrency(Pair.of(expenseCreateRequest.getUserUid(), expenseCreateRequest.getCurrency())).isPresent());
        Assertions.assertEquals(expenseDto.getAmount(), expenseDao.getByUid(expenseDto.getUid()).get().getAmount());
    }

    @Test
    public void testGetAllExpenses() throws Exception {
        List<ExpenseCategory> expenseCategories = expenseCategoryDao.getAll();
        List<Account> accounts = AccountMockGenerator.generateMockListOfAccounts(5);
        this.addMockAccounts(accounts);
        List<Expense> expenses = ExpenseMockGenerator.generateMockListOfExpenses(expenseCategories, accountDao.getAll(), 10);
        this.addMockExpenses(expenses);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/expense"))
                .andExpect(status().isOk())
                .andReturn();
        String expenseDtosJSON = result.getResponse().getContentAsString();
        List<ExpenseDto> expenseDtos = objectMapper.readValue(expenseDtosJSON, new TypeReference<List<ExpenseDto>>(){});

        Assertions.assertEquals(expenses.size(), expenseDtos.size());
        List<UUID> expensesUids = expenses.stream().map(Expense::getUid).toList();
        List<UUID> expenseDtosUids = expenseDtos.stream().map(ExpenseDto::getUid).toList();
        Assertions.assertEquals(expensesUids, expenseDtosUids);
    }

    @Test
    public void testUpdateExpense() throws Exception {
        List<ExpenseCategory> expenseCategories = expenseCategoryDao.getAll();
        List<Account> accounts = AccountMockGenerator.generateMockListOfAccounts(5);
        this.addMockAccounts(accounts);
        Expense expense = ExpenseMockGenerator.generateMockExpense(expenseCategories, accountDao.getAll());
        Expense savedExpense = expenseDao.save(expense);
        ExpenseUpdateRequest expenseUpdateRequest = ExpenseMockGenerator.generateMockExpenseUpdateRequest(expenseCategories);
        ExpenseCategory expenseUpdateRequestCategory = expenseCategoryDao.getByUid(expenseUpdateRequest.getCategoryUid()).get();
        String expenseUpdateRequestJSON = objectMapper.writeValueAsString(expenseUpdateRequest);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(String.format("/expense/%s", expense.getUid().toString()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expenseUpdateRequestJSON))
                .andExpect(status().isOk())
                .andReturn();
        String expenseDtoJSON = result.getResponse().getContentAsString();
        ExpenseDto expenseDto = objectMapper.readValue(expenseDtoJSON, ExpenseDto.class);

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
        List<ExpenseCategory> expenseCategories = expenseCategoryDao.getAll();
        List<Account> accounts = AccountMockGenerator.generateMockListOfAccounts(5);
        this.addMockAccounts(accounts);
        Expense expense = ExpenseMockGenerator.generateMockExpense(expenseCategories, accountDao.getAll());
        Expense savedExpense = expenseDao.save(expense);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete(String.format("/expense/%s", savedExpense.getUid().toString())))
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertEquals(0, expenseDao.getAll().size());
        Assertions.assertFalse(expenseDao.getById(savedExpense.getId()).isPresent());
    }

    private void addMockAccounts(List<Account> accounts) {
        accounts.forEach((account) -> accountDao.save(account));
    }

    private void addMockExpenses(List<Expense> expenses) {
        expenses.forEach(expense -> expenseDao.save(expense));
    }
}

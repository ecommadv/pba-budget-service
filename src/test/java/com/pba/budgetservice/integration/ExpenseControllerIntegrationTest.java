package com.pba.budgetservice.integration;

import com.PBA.budgetservice.controller.request.ExpenseCreateRequest;
import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.ExpenseCategory;
import com.PBA.budgetservice.persistance.model.dtos.ExpenseDto;
import com.PBA.budgetservice.persistance.repository.AccountDao;
import com.PBA.budgetservice.persistance.repository.ExpenseCategoryDao;
import com.PBA.budgetservice.persistance.repository.ExpenseDao;
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

    private void addMockAccounts(List<Account> accounts) {
        accounts.forEach((account) -> accountDao.save(account));
    }
}

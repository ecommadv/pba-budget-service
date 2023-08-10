package com.pba.budgetservice.integration;

import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.IncomeCategory;
import com.PBA.budgetservice.persistance.model.dtos.IncomeRequest;
import com.PBA.budgetservice.persistance.repository.AccountDao;
import com.PBA.budgetservice.persistance.repository.IncomeCategoryDao;
import com.PBA.budgetservice.persistance.repository.IncomeDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import mockgenerators.AccountMockGenerator;
import mockgenerators.IncomeMockGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

public class IncomeControllerIntegrationTest extends BaseControllerIntegrationTest {
    @Autowired
    private IncomeDao incomeDao;

    @Autowired
    private IncomeCategoryDao incomeCategoryDao;

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
    public void testCreateIncome() throws Exception {
        List<IncomeCategory> incomeCategoryList = incomeCategoryDao.getAll();
        List<Account> accounts = AccountMockGenerator.generateMockListOfAccounts(5);
        this.addMockAccounts(accounts);
        IncomeRequest incomeRequest = IncomeMockGenerator.generateMockIncomeRequest(incomeCategoryList, accounts);
        String incomeRequestJSON = objectMapper.writeValueAsString(incomeRequest);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/income")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(incomeRequestJSON)
                ).andExpect(status().isCreated())
                .andReturn();

        Assertions.assertEquals(1, incomeDao.getAll().size());
    }

    private void addMockAccounts(List<Account> accounts) {
        accounts.forEach(account -> accountDao.save(account));
    }
}

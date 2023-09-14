package com.pba.budgetservice.integration;

import com.PBA.budgetservice.controller.advice.ApiExceptionResponse;
import com.PBA.budgetservice.controller.request.IncomeUpdateRequest;
import com.PBA.budgetservice.exceptions.ErrorCodes;
import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.Income;
import com.PBA.budgetservice.persistance.model.IncomeCategory;
import com.PBA.budgetservice.persistance.model.dtos.IncomeCategoryDto;
import com.PBA.budgetservice.persistance.model.dtos.IncomeDto;
import com.PBA.budgetservice.controller.request.IncomeCreateRequest;
import com.PBA.budgetservice.persistance.repository.AccountDao;
import com.PBA.budgetservice.persistance.repository.IncomeCategoryDao;
import com.PBA.budgetservice.persistance.repository.IncomeDao;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import mockgenerators.AccountMockGenerator;
import mockgenerators.IncomeMockGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;
import java.util.UUID;

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
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void testCreateIncome() throws Exception {
        List<IncomeCategory> incomeCategoryList = incomeCategoryDao.getAll();
        List<Account> accounts = AccountMockGenerator.generateMockListOfAccounts(5);
        this.addMockAccounts(accounts);
        IncomeCreateRequest incomeRequest = IncomeMockGenerator.generateMockIncomeRequest(incomeCategoryList, accounts);
        String incomeRequestJSON = objectMapper.writeValueAsString(incomeRequest);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/income")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(incomeRequestJSON)
                ).andExpect(status().isCreated())
                .andReturn();
        String incomeDtoJSON = result.getResponse().getContentAsString();
        IncomeDto incomeDto = objectMapper.readValue(incomeDtoJSON, IncomeDto.class);

        Assertions.assertEquals(1, incomeDao.getAll().size());
        Assertions.assertTrue(accountDao.getByUserUidAndCurrency(Pair.of(incomeRequest.getUserUid(), incomeRequest.getCurrency())).isPresent());
        Assertions.assertEquals(incomeDto.getAmount(), incomeDao.getByUid(incomeDto.getUid()).get().getAmount());
    }

    @Test
    public void testGetAllIncomesByUserUidAndCurrency() throws Exception {
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

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(String.format("/income?userUid=%s&currency=%s", userUid.toString(), currency)))
                .andExpect(status().isOk())
                .andReturn();
        String incomeDtosJSON = result.getResponse().getContentAsString();
        List<IncomeDto> incomeDtos = objectMapper.readValue(incomeDtosJSON, new TypeReference<List<IncomeDto>>(){});
        Assertions.assertEquals(incomes.size(), incomeDtos.size());
        List<UUID> incomesUids = incomes.stream().map(Income::getUid).toList();
        List<UUID> incomeDtosUids = incomeDtos.stream().map(IncomeDto::getUid).toList();
        Assertions.assertEquals(incomesUids, incomeDtosUids);
    }

    @Test
    public void testGetAllIncomesByNonexistentUserUidAndCurrency() throws Exception {
        // given
        UUID nonexistentUid = UUID.randomUUID();
        String nonexistentCurrency = "RON";
        String getEndpoint = String.format("/income?userUid=%s&currency=%s", nonexistentUid, nonexistentCurrency);

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(getEndpoint))
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
        List<IncomeCategory> incomeCategoryList = incomeCategoryDao.getAll();
        List<Account> accounts = AccountMockGenerator.generateMockListOfAccounts(5);
        this.addMockAccounts(accounts);
        Income income = IncomeMockGenerator.generateMockIncome(incomeCategoryList, accountDao.getAll());
        Income savedIncome = incomeDao.save(income);
        IncomeUpdateRequest incomeUpdateRequest = IncomeMockGenerator.generateMockIncomeUpdateRequest(incomeCategoryList);
        IncomeCategory incomeUpdateRequestCategory = incomeCategoryDao.getIncomeCategoryByUid(incomeUpdateRequest.getCategoryUid()).get();
        String incomeUpdateRequestJSON = objectMapper.writeValueAsString(incomeUpdateRequest);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(String.format("/income/%s", income.getUid().toString()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(incomeUpdateRequestJSON))
                .andExpect(status().isOk())
                .andReturn();
        String incomeDtoJSON = result.getResponse().getContentAsString();
        IncomeDto incomeDto = objectMapper.readValue(incomeDtoJSON, IncomeDto.class);

        Income updatedIncome = incomeDao.getById(savedIncome.getId()).get();
        Assertions.assertEquals(incomeDto.getUid(), income.getUid());
        Assertions.assertNotEquals(savedIncome, incomeDao.getByUid(incomeDto.getUid()).get());
        Assertions.assertEquals(incomeUpdateRequest.getAmount(), updatedIncome.getAmount());
        Assertions.assertEquals(incomeUpdateRequest.getDescription(), updatedIncome.getDescription());
        Assertions.assertEquals(incomeUpdateRequestCategory.getId(), updatedIncome.getCategoryId());
    }

    @Test
    public void testDeleteIncome() throws Exception {
        List<IncomeCategory> incomeCategoryList = incomeCategoryDao.getAll();
        List<Account> accounts = AccountMockGenerator.generateMockListOfAccounts(5);
        this.addMockAccounts(accounts);
        Income income = IncomeMockGenerator.generateMockIncome(incomeCategoryList, accountDao.getAll());
        Income savedIncome = incomeDao.save(income);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete(String.format("/income/%s", savedIncome.getUid().toString())))
                .andExpect(status().isOk())
                .andReturn();

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
}
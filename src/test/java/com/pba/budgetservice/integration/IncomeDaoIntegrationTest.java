package com.pba.budgetservice.integration;

import com.PBA.budgetservice.exceptions.BudgetDaoException;
import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.Income;
import com.PBA.budgetservice.persistance.model.IncomeCategory;
import com.PBA.budgetservice.persistance.repository.AccountDao;
import com.PBA.budgetservice.persistance.repository.IncomeCategoryDao;
import com.PBA.budgetservice.persistance.repository.IncomeDao;
import mockgenerators.AccountMockGenerator;
import mockgenerators.IncomeCategoryMockGenerator;
import mockgenerators.IncomeMockGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class IncomeDaoIntegrationTest extends BaseDaoIntegrationTest {
    @Autowired
    private IncomeDao incomeDao;

    @Autowired
    private IncomeCategoryDao incomeCategoryDao;

    @Autowired
    private AccountDao accountDao;

    @Test
    public void testSave() {
        // given
        IncomeCategory incomeCategory = IncomeCategoryMockGenerator.generateMockIncomeCategory();
        Account account = AccountMockGenerator.generateMockAccount();
        incomeCategoryDao.save(incomeCategory);
        accountDao.save(account);
        Income income = IncomeMockGenerator.generateMockIncome(incomeCategoryDao.getAll(), accountDao.getAll());

        // when
        Income result = incomeDao.save(income);

        // then
        Assertions.assertEquals(income.getUid(), result.getUid());
        Assertions.assertEquals(1, incomeDao.getAll().size());
    }
    @Test
    public void testGetAll() {
        // given
        List<Account> accounts = AccountMockGenerator.generateMockListOfAccounts(10);
        List<IncomeCategory> incomeCategories = IncomeCategoryMockGenerator.generateMockListOfIncomeCategories(10);
        this.addMockListOfAccounts(accounts);
        this.addMockListOfIncomeCategories(incomeCategories);
        List<Income> incomes = IncomeMockGenerator.generateMockListOfIncomes(incomeCategoryDao.getAll(), accountDao.getAll(), 10);
        this.addMockListOfIncomes(incomes);
        List<UUID> incomeUidsExpected = this.extractIncomeUids(incomes);


        // when
        List<Income> result = incomeDao.getAll();
        List<UUID> incomeUidsResult = this.extractIncomeUids(result);

        // then
        Assertions.assertEquals(incomeUidsExpected, incomeUidsResult);
    }

    @Test
    public void testGetPresentById() {
        // given
        IncomeCategory incomeCategory = IncomeCategoryMockGenerator.generateMockIncomeCategory();
        Account account = AccountMockGenerator.generateMockAccount();
        incomeCategoryDao.save(incomeCategory);
        accountDao.save(account);
        Income income = IncomeMockGenerator.generateMockIncome(incomeCategoryDao.getAll(), accountDao.getAll());
        Income savedIncome = incomeDao.save(income);

        // when
        Optional<Income> result = incomeDao.getById(savedIncome.getId());

        // then
        Assertions.assertEquals(savedIncome, result.get());
    }

    @Test
    public void testGetAbsentById() {
        // given
        Long id = new Random().nextLong();

        // when
        Optional<Income> result = incomeDao.getById(id);

        // then
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testDeletePresentById() {
        // given
        IncomeCategory incomeCategory = IncomeCategoryMockGenerator.generateMockIncomeCategory();
        Account account = AccountMockGenerator.generateMockAccount();
        incomeCategoryDao.save(incomeCategory);
        accountDao.save(account);
        Income income = IncomeMockGenerator.generateMockIncome(incomeCategoryDao.getAll(), accountDao.getAll());
        Income savedIncome = incomeDao.save(income);

        // when
        Income result = incomeDao.deleteById(savedIncome.getId());

        // then
        Assertions.assertEquals(savedIncome, result);
        Assertions.assertEquals(0, incomeDao.getAll().size());
    }

    @Test
    public void testDeleteAbsentById() {
        Long id = new Random().nextLong();

        assertThatThrownBy(() -> incomeDao.deleteById(id))
                .isInstanceOf(BudgetDaoException.class)
                .hasMessage(String.format("Object with id %s is not stored!", id.toString()));
    }

    @Test
    public void testUpdatePresent() {
        // given
        IncomeCategory incomeCategory = IncomeCategoryMockGenerator.generateMockIncomeCategory();
        Account account = AccountMockGenerator.generateMockAccount();
        incomeCategoryDao.save(incomeCategory);
        accountDao.save(account);
        Income income = IncomeMockGenerator.generateMockIncome(incomeCategoryDao.getAll(), accountDao.getAll());
        Income savedIncome = incomeDao.save(income);
        Income newIncome = IncomeMockGenerator.generateMockIncome(incomeCategoryDao.getAll(), accountDao.getAll());
        newIncome.setId(savedIncome.getId());

        // when
        Income result = incomeDao.update(newIncome, savedIncome.getId());

        // then
        Assertions.assertEquals(newIncome, result);
        Assertions.assertEquals(newIncome, incomeDao.getById(savedIncome.getId()).get());
    }

    @Test
    public void testUpdateAbsent() {
        IncomeCategory incomeCategory = IncomeCategoryMockGenerator.generateMockIncomeCategory();
        Account account = AccountMockGenerator.generateMockAccount();
        incomeCategoryDao.save(incomeCategory);
        accountDao.save(account);
        Income income = IncomeMockGenerator.generateMockIncome(incomeCategoryDao.getAll(), accountDao.getAll());

        assertThatThrownBy(() -> incomeDao.update(income, income.getId()))
                .isInstanceOf(BudgetDaoException.class)
                .hasMessage(String.format("Object with id %d is not stored!", income.getId()));
    }

    private void addMockListOfAccounts(List<Account> accounts) {
        for (Account account : accounts) {
            accountDao.save(account);
        }
    }

    private void addMockListOfIncomeCategories(List<IncomeCategory> incomeCategories) {
        for (IncomeCategory incomeCategory : incomeCategories) {
            incomeCategoryDao.save(incomeCategory);
        }
    }

    private void addMockListOfIncomes(List<Income> incomes) {
        for (Income income : incomes) {
            incomeDao.save(income);
        }
    }

    private List<UUID> extractIncomeUids(List<Income> incomes) {
        return incomes.stream().map(Income::getUid).collect(Collectors.toList());
    }
}

package com.pba.budgetservice.integration;

import com.PBA.budgetservice.exceptions.BudgetDaoException;
import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.Expense;
import com.PBA.budgetservice.persistance.model.ExpenseCategory;
import com.PBA.budgetservice.persistance.repository.AccountDao;
import com.PBA.budgetservice.persistance.repository.ExpenseCategoryDao;
import com.PBA.budgetservice.persistance.repository.ExpenseDao;
import mockgenerators.AccountMockGenerator;
import mockgenerators.ExpenseCategoryMockGenerator;
import mockgenerators.ExpenseMockGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ExpenseDaoIntegrationTest extends BaseDaoIntegrationTest {
    @Autowired
    private ExpenseDao expenseDao;
    @Autowired
    private ExpenseCategoryDao expenseCategoryDao;
    @Autowired
    private AccountDao accountDao;

    @Test
    public void testSave() {
        // given
        ExpenseCategory expenseCategory = ExpenseCategoryMockGenerator.generateMockExpenseCategory();
        Account account = AccountMockGenerator.generateMockAccount();
        expenseCategoryDao.save(expenseCategory);
        accountDao.save(account);
        Expense expense = ExpenseMockGenerator.generateMockExpense(expenseCategoryDao.getAll(), accountDao.getAll());

        // when
        Expense result = expenseDao.save(expense);

        // then
        Assertions.assertEquals(expense.getUid(), result.getUid());
        Assertions.assertEquals(1, expenseDao.getAll().size());
    }
    @Test
    public void testGetAll() {
        // given
        List<Account> accounts = AccountMockGenerator.generateMockListOfAccounts(10);
        List<ExpenseCategory> expenseCategories = ExpenseCategoryMockGenerator.generateMockListOfExpenseCategories(10);
        this.addMockListOfAccounts(accounts);
        this.addMockListOfExpenseCategories(expenseCategories);
        List<Expense> expenses = ExpenseMockGenerator.generateMockListOfExpenses(expenseCategoryDao.getAll(), accountDao.getAll(), 10);
        this.addMockListOfExpenses(expenses);
        List<UUID> expenseUidsExpected = this.extractExpenseUids(expenses);


        // when
        List<Expense> result = expenseDao.getAll();
        List<UUID> expenseUidsResult = this.extractExpenseUids(result);

        // then
        Assertions.assertEquals(expenseUidsExpected, expenseUidsResult);
    }

    @Test
    public void testGetPresentById() {
        // given
        ExpenseCategory expenseCategory = ExpenseCategoryMockGenerator.generateMockExpenseCategory();
        Account account = AccountMockGenerator.generateMockAccount();
        expenseCategoryDao.save(expenseCategory);
        accountDao.save(account);
        Expense expense = ExpenseMockGenerator.generateMockExpense(expenseCategoryDao.getAll(), accountDao.getAll());
        Expense savedExpense = expenseDao.save(expense);

        // when
        Optional<Expense> result = expenseDao.getById(savedExpense.getId());

        // then
        Assertions.assertEquals(savedExpense, result.get());
    }

    @Test
    public void testGetAbsentById() {
        // given
        Long id = new Random().nextLong();

        // when
        Optional<Expense> result = expenseDao.getById(id);

        // then
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testDeletePresentById() {
        // given
        ExpenseCategory expenseCategory = ExpenseCategoryMockGenerator.generateMockExpenseCategory();
        Account account = AccountMockGenerator.generateMockAccount();
        expenseCategoryDao.save(expenseCategory);
        accountDao.save(account);
        Expense expense = ExpenseMockGenerator.generateMockExpense(expenseCategoryDao.getAll(), accountDao.getAll());
        Expense savedExpense = expenseDao.save(expense);

        // when
        Expense result = expenseDao.deleteById(savedExpense.getId());

        // then
        Assertions.assertEquals(savedExpense, result);
        Assertions.assertEquals(0, expenseDao.getAll().size());
    }

    @Test
    public void testDeleteAbsentById() {
        Long id = new Random().nextLong();

        assertThatThrownBy(() -> expenseDao.deleteById(id))
                .isInstanceOf(BudgetDaoException.class)
                .hasMessage(String.format("Object with id %s is not stored!", id.toString()));
    }

    @Test
    public void testUpdatePresent() {
        // given
        ExpenseCategory expenseCategory = ExpenseCategoryMockGenerator.generateMockExpenseCategory();
        Account account = AccountMockGenerator.generateMockAccount();
        expenseCategoryDao.save(expenseCategory);
        accountDao.save(account);
        Expense expense = ExpenseMockGenerator.generateMockExpense(expenseCategoryDao.getAll(), accountDao.getAll());
        Expense savedExpense = expenseDao.save(expense);
        Expense newExpense = ExpenseMockGenerator.generateMockExpense(expenseCategoryDao.getAll(), accountDao.getAll());
        newExpense.setId(savedExpense.getId());

        // when
        Expense result = expenseDao.update(newExpense, savedExpense.getId());

        // then
        Assertions.assertEquals(newExpense, result);
        Assertions.assertEquals(newExpense, expenseDao.getById(savedExpense.getId()).get());
    }

    @Test
    public void testUpdateAbsent() {
        ExpenseCategory expenseCategory = ExpenseCategoryMockGenerator.generateMockExpenseCategory();
        Account account = AccountMockGenerator.generateMockAccount();
        expenseCategoryDao.save(expenseCategory);
        accountDao.save(account);
        Expense expense = ExpenseMockGenerator.generateMockExpense(expenseCategoryDao.getAll(), accountDao.getAll());

        assertThatThrownBy(() -> expenseDao.update(expense, expense.getId()))
                .isInstanceOf(BudgetDaoException.class)
                .hasMessage(String.format("Object with id %d is not stored!", expense.getId()));
    }

    private void addMockListOfAccounts(List<Account> accounts) {
        for (Account account : accounts) {
            accountDao.save(account);
        }
    }

    private void addMockListOfExpenseCategories(List<ExpenseCategory> expenseCategories) {
        for (ExpenseCategory expenseCategory : expenseCategories) {
            expenseCategoryDao.save(expenseCategory);
        }
    }

    private void addMockListOfExpenses(List<Expense> expenses) {
        for (Expense expense : expenses) {
            expenseDao.save(expense);
        }
    }

    private List<UUID> extractExpenseUids(List<Expense> expenses) {
        return expenses.stream().map(Expense::getUid).collect(Collectors.toList());
    }
}

package com.pba.budgetservice.integration;

import com.PBA.budgetservice.exceptions.BudgetDaoException;
import com.PBA.budgetservice.persistance.model.ExpenseCategory;
import com.PBA.budgetservice.persistance.repository.ExpenseCategoryDao;
import com.pba.budgetservice.mockgenerators.ExpenseCategoryMockGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ExpenseCategoryDaoIntegrationTest extends BaseJdbcDaoIntegrationTest {
    @Autowired
    private ExpenseCategoryDao expenseCategoryDao;

    @Test
    public void testSave() {
        // given
        ExpenseCategory expenseCategory = ExpenseCategoryMockGenerator.generateMockExpenseCategory();

        // when
        ExpenseCategory result = expenseCategoryDao.save(expenseCategory);

        // then
        Assertions.assertEquals(expenseCategory.getUid(), result.getUid());
        Assertions.assertEquals(1, expenseCategoryDao.getAll().size());
    }
    @Test
    public void testGetAll() {
        // given
        List<ExpenseCategory> expenseCategories = ExpenseCategoryMockGenerator.generateMockListOfExpenseCategories(10);
        this.addMockListOfExpenseCategory(expenseCategories);
        List<UUID> expenseCategoriesUids = this.extractUids(expenseCategories);

        // when
        List<ExpenseCategory> result = expenseCategoryDao.getAll();
        List<UUID> resultUids = this.extractUids(result);

        // then
        Assertions.assertEquals(expenseCategoriesUids, resultUids);
    }

    @Test
    public void testGetPresentById() {
        // given
        ExpenseCategory expenseCategory = ExpenseCategoryMockGenerator.generateMockExpenseCategory();
        ExpenseCategory savedExpenseCategory = expenseCategoryDao.save(expenseCategory);

        // when
        Optional<ExpenseCategory> result = expenseCategoryDao.getById(savedExpenseCategory.getId());

        // then
        Assertions.assertEquals(savedExpenseCategory, result.get());
    }

    @Test
    public void testGetAbsentById() {
        // given
        Long id = new Random().nextLong();

        // when
        Optional<ExpenseCategory> result = expenseCategoryDao.getById(id);

        // then
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testDeletePresentById() {
        // given
        ExpenseCategory expenseCategory = ExpenseCategoryMockGenerator.generateMockExpenseCategory();
        ExpenseCategory savedExpenseCategory = expenseCategoryDao.save(expenseCategory);

        // when
        ExpenseCategory result = expenseCategoryDao.deleteById(savedExpenseCategory.getId());

        // then
        Assertions.assertEquals(savedExpenseCategory, result);
        Assertions.assertEquals(0, expenseCategoryDao.getAll().size());
    }

    @Test
    public void testDeleteAbsentById() {
        Long id = new Random().nextLong();

        assertThatThrownBy(() -> expenseCategoryDao.deleteById(id))
                .isInstanceOf(BudgetDaoException.class)
                .hasMessage(String.format("Object with id %s is not stored!", id.toString()));
    }

    @Test
    public void testUpdatePresent() {
        // given
        ExpenseCategory expenseCategory = ExpenseCategoryMockGenerator.generateMockExpenseCategory();
        ExpenseCategory newExpenseCategory = ExpenseCategoryMockGenerator.generateMockExpenseCategory();
        ExpenseCategory savedExpenseCategory = expenseCategoryDao.save(expenseCategory);

        // when
        ExpenseCategory result = expenseCategoryDao.update(newExpenseCategory, savedExpenseCategory.getId());

        // then
        Assertions.assertEquals(newExpenseCategory.getUid(), result.getUid());
        Assertions.assertEquals(newExpenseCategory.getUid(), expenseCategoryDao.getById(savedExpenseCategory.getId()).get().getUid());
    }

    @Test
    public void testUpdateAbsent() {
        ExpenseCategory expenseCategory = ExpenseCategoryMockGenerator.generateMockExpenseCategory();

        assertThatThrownBy(() -> expenseCategoryDao.update(expenseCategory, expenseCategory.getId()))
                .isInstanceOf(BudgetDaoException.class)
                .hasMessage(String.format("Object with id %d is not stored!", expenseCategory.getId()));
    }

    private void addMockListOfExpenseCategory(List<ExpenseCategory> expenseCategories) {
        for (ExpenseCategory expenseCategory : expenseCategories) {
            expenseCategoryDao.save(expenseCategory);
        }
    }

    private List<UUID> extractUids(List<ExpenseCategory> expenseCategories) {
        return expenseCategories.stream().map(ExpenseCategory::getUid).collect(Collectors.toList());
    }
}

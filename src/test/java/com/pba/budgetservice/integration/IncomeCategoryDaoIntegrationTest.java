package com.pba.budgetservice.integration;

import com.PBA.budgetservice.exceptions.BudgetDaoException;
import com.PBA.budgetservice.persistance.model.IncomeCategory;
import com.PBA.budgetservice.persistance.repository.IncomeCategoryDao;
import mockgenerators.IncomeCategoryMockGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class IncomeCategoryDaoIntegrationTest extends BaseJdbcDaoIntegrationTest {
    @Autowired
    private IncomeCategoryDao incomeCategoryDao;

    @Test
    public void testSave() {
        // given
        IncomeCategory incomeCategory = IncomeCategoryMockGenerator.generateMockIncomeCategory();

        // when
        IncomeCategory result = incomeCategoryDao.save(incomeCategory);

        // then
        Assertions.assertEquals(incomeCategory.getUid(), result.getUid());
        Assertions.assertEquals(1, incomeCategoryDao.getAll().size());
    }
    @Test
    public void testGetAll() {
        // given
        List<IncomeCategory> incomeCategories = IncomeCategoryMockGenerator.generateMockListOfIncomeCategories(10);
        this.addMockListOfIncomeCategory(incomeCategories);
        List<UUID> incomeCategoriesUids = this.extractUids(incomeCategories);

        // when
        List<IncomeCategory> result = incomeCategoryDao.getAll();
        List<UUID> resultUids = this.extractUids(result);

        // then
        Assertions.assertEquals(incomeCategoriesUids, resultUids);
    }

    @Test
    public void testGetPresentById() {
        // given
        IncomeCategory incomeCategory = IncomeCategoryMockGenerator.generateMockIncomeCategory();
        IncomeCategory savedIncomeCategory = incomeCategoryDao.save(incomeCategory);

        // when
        Optional<IncomeCategory> result = incomeCategoryDao.getById(savedIncomeCategory.getId());

        // then
        Assertions.assertEquals(savedIncomeCategory, result.get());
    }

    @Test
    public void testGetAbsentById() {
        // given
        Long id = new Random().nextLong();

        // when
        Optional<IncomeCategory> result = incomeCategoryDao.getById(id);

        // then
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testDeletePresentById() {
        // given
        IncomeCategory incomeCategory = IncomeCategoryMockGenerator.generateMockIncomeCategory();
        IncomeCategory savedIncomeCategory = incomeCategoryDao.save(incomeCategory);

        // when
        IncomeCategory result = incomeCategoryDao.deleteById(savedIncomeCategory.getId());

        // then
        Assertions.assertEquals(savedIncomeCategory, result);
        Assertions.assertEquals(0, incomeCategoryDao.getAll().size());
    }

    @Test
    public void testDeleteAbsentById() {
        Long id = new Random().nextLong();

        assertThatThrownBy(() -> incomeCategoryDao.deleteById(id))
                .isInstanceOf(BudgetDaoException.class)
                .hasMessage(String.format("Object with id %s is not stored!", id.toString()));
    }

    @Test
    public void testUpdatePresent() {
        // given
        IncomeCategory incomeCategory = IncomeCategoryMockGenerator.generateMockIncomeCategory();
        IncomeCategory newIncomeCategory = IncomeCategoryMockGenerator.generateMockIncomeCategory();
        IncomeCategory savedIncomeCategory = incomeCategoryDao.save(incomeCategory);

        // when
        IncomeCategory result = incomeCategoryDao.update(newIncomeCategory, savedIncomeCategory.getId());

        // then
        Assertions.assertEquals(newIncomeCategory.getUid(), result.getUid());
        Assertions.assertEquals(newIncomeCategory.getUid(), incomeCategoryDao.getById(savedIncomeCategory.getId()).get().getUid());
    }

    @Test
    public void testUpdateAbsent() {
        IncomeCategory incomeCategory = IncomeCategoryMockGenerator.generateMockIncomeCategory();

        assertThatThrownBy(() -> incomeCategoryDao.update(incomeCategory, incomeCategory.getId()))
                .isInstanceOf(BudgetDaoException.class)
                .hasMessage(String.format("Object with id %d is not stored!", incomeCategory.getId()));
    }

    private void addMockListOfIncomeCategory(List<IncomeCategory> incomeCategories) {
        for (IncomeCategory incomeCategory : incomeCategories) {
            incomeCategoryDao.save(incomeCategory);
        }
    }

    private List<UUID> extractUids(List<IncomeCategory> incomeCategories) {
        return incomeCategories.stream().map(IncomeCategory::getUid).collect(Collectors.toList());
    }
}

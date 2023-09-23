package com.pba.budgetservice.integration;

import com.PBA.budgetservice.exceptions.BudgetDaoException;
import com.PBA.budgetservice.persistance.model.CurrencyRate;
import com.PBA.budgetservice.persistance.repository.CurrencyRateDao;
import com.pba.budgetservice.mockgenerators.CurrencyRateMockGenerator;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

public class CurrencyRateDaoIntegrationTest extends BaseMongoDaoIntegrationTest {
    @Autowired
    private CurrencyRateDao currencyRateDao;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    public void cleanup() {
        mongoTemplate.dropCollection(CurrencyRate.class);
    }

    @Test
    public void testSave() {
        // given
        CurrencyRate currencyRate = CurrencyRateMockGenerator.generateMockCurrencyRate();

        // when
        CurrencyRate savedCurrencyRate = currencyRateDao.save(currencyRate);

        // then
        assertEquals(1, currencyRateDao.getAll().size());
        assertEquals(currencyRate.getCode(), currencyRateDao.getById(savedCurrencyRate.getId()).get().getCode());
        assertEquals(currencyRate.getMainValue(), currencyRateDao.getById(savedCurrencyRate.getId()).get().getMainValue());
    }

    @Test
    public void testGetAll() {
        // given
        final int sampleSize = 10;
        List<CurrencyRate> currencyRates = CurrencyRateMockGenerator.generateMockListOfCurrencyRates(sampleSize);
        this.saveCurrencyRates(currencyRates);
        List<String> expectedCodes = this.extractCodes(currencyRates);
        List<BigDecimal> expectedRonValues = this.extractRonValues(currencyRates);

        // when
        List<CurrencyRate> result = currencyRateDao.getAll();
        List<String> resultedCodes = this.extractCodes(result);
        List<BigDecimal> resultedRonValues = this.extractRonValues(result);

        // then
        assertEquals(sampleSize, result.size());
        assertEquals(expectedCodes, resultedCodes);
        assertEquals(expectedRonValues, resultedRonValues);
    }

    @Test
    public void testGetPresentById() {
        // given
        CurrencyRate currencyRate = CurrencyRateMockGenerator.generateMockCurrencyRate();
        CurrencyRate savedCurrencyRate = currencyRateDao.save(currencyRate);

        // when
        Optional<CurrencyRate> result = currencyRateDao.getById(savedCurrencyRate.getId());

        // then
        assertTrue(result.isPresent());
        assertEquals(savedCurrencyRate, result.get());
    }

    @Test
    public void testGetAbsentById() {
        // given
        String nonexistentId = "";

        // when
        Optional<CurrencyRate> result = currencyRateDao.getById(nonexistentId);

        // then
        assertFalse(result.isPresent());
    }

    @Test
    public void testDeletePresentById() {
        // given
        CurrencyRate currencyRate = CurrencyRateMockGenerator.generateMockCurrencyRate();
        CurrencyRate savedCurrencyRate = currencyRateDao.save(currencyRate);

        // when
        long deletedCount = currencyRateDao.deleteById(savedCurrencyRate.getId());

        // then
        assertEquals(1, deletedCount);
        assertEquals(0, currencyRateDao.getAll().size());
    }

    @Test
    public void testDeleteAbsentById() {
        // given
        String nonexistentId = "";

        // when
        ThrowableAssert.ThrowingCallable supplier = () -> currencyRateDao.deleteById(nonexistentId);

        // then
        assertThatThrownBy(supplier)
                .isInstanceOf(BudgetDaoException.class)
                .hasMessage(String.format("Currency rate with id %s does not exist", nonexistentId));
    }

    @Test
    public void testUpdatePresent() {
        // given
        CurrencyRate currencyRate = CurrencyRateMockGenerator.generateMockCurrencyRate();
        CurrencyRate savedCurrencyRate = currencyRateDao.save(currencyRate);
        CurrencyRate newCurrencyRate = CurrencyRateMockGenerator.generateMockCurrencyRate();

        // when
        long updatedCount = currencyRateDao.update(newCurrencyRate, savedCurrencyRate.getId());

        // then
        assertEquals(1, updatedCount);
        assertEquals(newCurrencyRate.getCode(), currencyRateDao.getById(savedCurrencyRate.getId()).get().getCode());
        assertEquals(newCurrencyRate.getMainValue(), currencyRateDao.getById(savedCurrencyRate.getId()).get().getMainValue());
    }

    @Test
    public void testUpdateAbsent() {
        // given
        String nonexistentId = "";
        CurrencyRate currencyRate = CurrencyRateMockGenerator.generateMockCurrencyRate();

        // when
        ThrowableAssert.ThrowingCallable supplier = () -> currencyRateDao.update(currencyRate, nonexistentId);

        // then
        assertThatThrownBy(supplier)
                .isInstanceOf(BudgetDaoException.class)
                .hasMessage(String.format("Currency rate with id %s does not exist", nonexistentId));
    }

    private void saveCurrencyRates(List<CurrencyRate> currencyRates) {
        currencyRates.forEach(currencyRate -> currencyRateDao.save(currencyRate));
    }

    private List<String> extractCodes(List<CurrencyRate> currencyRates) {
        return currencyRates
                .stream()
                .map(CurrencyRate::getCode)
                .collect(Collectors.toList());
    }

    private List<BigDecimal> extractRonValues(List<CurrencyRate> currencyRates) {
        return currencyRates
                .stream()
                .map(CurrencyRate::getMainValue)
                .collect(Collectors.toList());
    }

}

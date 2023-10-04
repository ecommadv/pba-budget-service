package com.PBA.budgetservice.service;

import com.PBA.budgetservice.exceptions.EntityNotFoundException;
import com.PBA.budgetservice.exceptions.ErrorCodes;
import com.PBA.budgetservice.persistance.model.CurrencyRate;
import com.PBA.budgetservice.persistance.repository.CurrencyRateDao;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Service
public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyRateDao currencyRateDao;

    public CurrencyServiceImpl(CurrencyRateDao currencyRateDao) {
        this.currencyRateDao = currencyRateDao;
    }

    @Override
    public CurrencyRate addCurrencyRate(CurrencyRate currencyRate) {
        return currencyRateDao.save(currencyRate);
    }

    @Override
    public long deleteAllCurrencyRates() {
        return currencyRateDao
                .getAll()
                .stream()
                .mapToLong((currencyRate) -> currencyRateDao.deleteById(currencyRate.getId()))
                .reduce(0, Long::sum);
    }

    @Override
    public boolean currencyRateWithCodeExists(String code) {
        return currencyRateDao
                .getAll()
                .stream()
                .anyMatch(currencyRate -> currencyRate.getCode().equals(code));
    }

    @Override
    public CurrencyRate getCurrencyRateByCode(String code) {
        return currencyRateDao.getByCode(code)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Currency with code %s does not exist", code),
                        ErrorCodes.CURRENCY_NOT_FOUND
                ));
    }

    @Override
    public BigDecimal convertCurrency(CurrencyRate from, CurrencyRate to) {
        return from.getMainValue().divide(to.getMainValue(), new MathContext(5));
    }
}

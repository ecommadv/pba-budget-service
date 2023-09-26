package com.PBA.budgetservice.service;

import com.PBA.budgetservice.persistance.model.CurrencyRate;
import com.PBA.budgetservice.persistance.repository.CurrencyRateDao;
import org.springframework.stereotype.Service;

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
}

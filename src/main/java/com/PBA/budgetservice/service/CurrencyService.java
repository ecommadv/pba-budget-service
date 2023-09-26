package com.PBA.budgetservice.service;

import com.PBA.budgetservice.persistance.model.CurrencyRate;

public interface CurrencyService {
    public CurrencyRate addCurrencyRate(CurrencyRate currencyRate);
    public long deleteAllCurrencyRates();
    public boolean currencyRateWithCodeExists(String code);
}

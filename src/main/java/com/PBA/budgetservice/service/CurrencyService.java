package com.PBA.budgetservice.service;

import com.PBA.budgetservice.persistance.model.CurrencyRate;

import java.math.BigDecimal;

public interface CurrencyService {
    public CurrencyRate addCurrencyRate(CurrencyRate currencyRate);
    public long deleteAllCurrencyRates();
    public CurrencyRate getCurrencyRateByCode(String code);
    public BigDecimal convertCurrency(CurrencyRate from, CurrencyRate to);
    public boolean currencyRateWithCodeExists(String code);
}

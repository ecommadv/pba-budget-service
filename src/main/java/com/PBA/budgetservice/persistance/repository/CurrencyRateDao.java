package com.PBA.budgetservice.persistance.repository;

import com.PBA.budgetservice.persistance.model.CurrencyRate;

import java.util.List;
import java.util.Optional;

public interface CurrencyRateDao {
    public CurrencyRate save(CurrencyRate currencyRate);
    public Optional<CurrencyRate> getById(String id);
    public List<CurrencyRate> getAll();
    public long deleteById(String id);
    public long update(CurrencyRate currencyRate, String id);
    public Optional<CurrencyRate> getByCode(String code);
}

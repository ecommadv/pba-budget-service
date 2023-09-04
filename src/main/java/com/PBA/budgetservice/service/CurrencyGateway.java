package com.PBA.budgetservice.service;

import java.math.BigDecimal;
import java.util.Map;

public interface CurrencyGateway {
    public Map<String, BigDecimal> getCurrencyExchangeRatesMapping();
}

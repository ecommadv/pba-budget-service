package com.PBA.budgetservice.gateway;

import java.math.BigDecimal;
import java.util.Map;

public interface CurrencyGateway {
    public Map<String, BigDecimal> getCurrencyExchangeRatesMapping();
}

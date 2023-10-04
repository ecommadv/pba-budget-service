package com.PBA.budgetservice.facade;

import com.PBA.budgetservice.controller.request.CurrencyConversionRequest;
import com.PBA.budgetservice.controller.response.CurrencyConversionResponse;

public interface CurrencyFacade {
    public CurrencyConversionResponse convertCurrency(CurrencyConversionRequest currencyConversionRequest);
}

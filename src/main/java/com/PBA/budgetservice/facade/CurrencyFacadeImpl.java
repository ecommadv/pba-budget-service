package com.PBA.budgetservice.facade;

import com.PBA.budgetservice.controller.request.CurrencyConversionRequest;
import com.PBA.budgetservice.controller.response.CurrencyConversionResponse;
import com.PBA.budgetservice.mapper.CurrencyMapper;
import com.PBA.budgetservice.persistance.model.CurrencyRate;
import com.PBA.budgetservice.service.CurrencyService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CurrencyFacadeImpl implements CurrencyFacade {
    private final CurrencyService currencyService;
    private final CurrencyMapper currencyMapper;

    public CurrencyFacadeImpl(CurrencyService currencyService, CurrencyMapper currencyMapper) {
        this.currencyService = currencyService;
        this.currencyMapper = currencyMapper;
    }

    @Override
    public CurrencyConversionResponse convertCurrency(CurrencyConversionRequest currencyConversionRequest) {
        CurrencyRate convertFrom = currencyService.getCurrencyRateByCode(currencyConversionRequest.getFrom());
        CurrencyRate convertTo = currencyService.getCurrencyRateByCode(currencyConversionRequest.getTo());
        BigDecimal convertedValue = currencyService.convertCurrency(convertFrom, convertTo);
        return currencyMapper.toConversionResponse(currencyConversionRequest, convertedValue);
    }
}

package com.PBA.budgetservice.controller;

import com.PBA.budgetservice.service.CurrencyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.Map;

@Controller
public class CurrencyControllerImpl implements CurrencyController {
    private final CurrencyService currencyService;

    public CurrencyControllerImpl(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Override
    public ResponseEntity<Map<String, BigDecimal>> getCurrencyExchangeRates() {
        return new ResponseEntity<>(currencyService.getCurrencyExchangeRatesMapping(), HttpStatus.OK);
    }
}

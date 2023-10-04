package com.PBA.budgetservice.controller;

import com.PBA.budgetservice.controller.request.CurrencyConversionRequest;
import com.PBA.budgetservice.controller.response.CurrencyConversionResponse;
import com.PBA.budgetservice.facade.CurrencyFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class CurrencyControllerImpl implements CurrencyController {
    private final CurrencyFacade currencyFacade;

    public CurrencyControllerImpl(CurrencyFacade currencyFacade) {
        this.currencyFacade = currencyFacade;
    }

    @Override
    public ResponseEntity<CurrencyConversionResponse> convertCurrency(CurrencyConversionRequest currencyConversionRequest) {
        CurrencyConversionResponse currencyConversionResponse = currencyFacade.convertCurrency(currencyConversionRequest);
        return new ResponseEntity<>(currencyConversionResponse, HttpStatus.OK);
    }
}

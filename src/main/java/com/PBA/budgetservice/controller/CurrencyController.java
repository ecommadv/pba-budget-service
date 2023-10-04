package com.PBA.budgetservice.controller;

import com.PBA.budgetservice.controller.request.CurrencyConversionRequest;
import com.PBA.budgetservice.controller.response.CurrencyConversionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/currency")
public interface CurrencyController {
    @Operation(summary = "Converts one currency to another and provides the resulted value.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping("/convert")
    public ResponseEntity<CurrencyConversionResponse> convertCurrency(@Valid @RequestBody CurrencyConversionRequest currencyConversionRequest);
}

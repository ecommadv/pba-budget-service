package com.PBA.budgetservice.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CurrencyConversionResponse {
    @Schema(example = "EUR")
    private String from;

    @Schema(example = "USD")
    private String to;

    @Schema(example = "1.07")
    private BigDecimal convertedValue;
}

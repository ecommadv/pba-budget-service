package com.PBA.budgetservice.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CurrencyConversionRequest {
    @NotBlank(message = "{fromCurrency.notblank}")
    @Schema(example = "EUR")
    private String from;

    @NotBlank(message = "{toCurrency.notblank}")
    @Schema(example = "USD")
    private String to;
}

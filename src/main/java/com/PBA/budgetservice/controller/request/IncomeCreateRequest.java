package com.PBA.budgetservice.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class IncomeCreateRequest {
    @NotNull(message = "{amount.notnull}")
    @Positive(message = "{amount.positive}")
    @Schema(example = "500.32")
    private BigDecimal amount;

    @NotBlank(message = "{description.notblank}")
    @Size(max = 500, message = "{description.maxsize}")
    @Schema(example = "Income for house invoice")
    private String description;

    @NotBlank(message = "{currency.notblank}")
    @Schema(example = "RON")
    private String currency;

    @NotNull(message = "{categoryUid.notnull}")
    @Schema(example = "4gf23f64-2341-2314-a4fd-5a432g33dab4")
    private UUID categoryUid;
}

package com.PBA.budgetservice.controller.request;

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
    private BigDecimal amount;

    @NotBlank(message = "{description.notblank}")
    @Size(max = 500, message = "{description.maxsize}")
    private String description;

    @NotBlank(message = "{currency.notblank}")
    private String currency;

    @NotNull(message = "{userUid.notnull}")
    private UUID userUid;

    @NotNull(message = "{categoryUid.notnull}")
    private UUID categoryUid;
}

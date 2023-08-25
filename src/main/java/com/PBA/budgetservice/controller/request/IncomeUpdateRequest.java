package com.PBA.budgetservice.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class IncomeUpdateRequest {
    @Schema(example = "500.32")
    private BigDecimal amount;

    @Schema(example = "Income for house invoice")
    private String description;

    @Schema(example = "4gf23f64-2341-2314-a4fd-5a432g33dab4")
    private UUID categoryUid;
}

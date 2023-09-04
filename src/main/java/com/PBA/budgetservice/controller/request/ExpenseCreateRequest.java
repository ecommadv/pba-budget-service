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
public class ExpenseCreateRequest {
    @Schema(example = "500.24")
    private BigDecimal amount;

    @Schema(example = "House invoice")
    private String name;

    @Schema(example = "Expense for house invoice")
    private String description;

    @Schema(example = "RON")
    private String currency;

    @Schema(example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID userUid;

    @Schema(example = "4gf23f64-2341-2314-a4fd-5a432g33dab4")
    private UUID categoryUid;
}

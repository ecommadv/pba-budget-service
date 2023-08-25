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
public class ExpenseUpdateRequest {
    @Schema(example = "500.32")
    private BigDecimal amount;

    @Schema(example = "House invoice")
    private String name;

    @Schema(example = "Expense for house invoice")
    private String description;

    @Schema(example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID categoryUid;
}

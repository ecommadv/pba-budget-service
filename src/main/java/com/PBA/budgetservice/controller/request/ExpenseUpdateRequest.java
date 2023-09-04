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
public class ExpenseUpdateRequest {
    @NotNull(message = "{amount.notnull}")
    @Positive(message = "{amount.positive}")
    @Schema(example = "500.32")
    private BigDecimal amount;

    @NotBlank(message = "{name.notblank}")
    @Size(max = 50, message = "{name.maxsize}")
    @Schema(example = "House invoice")
    private String name;

    @NotBlank(message = "{description.notblank}")
    @Size(max = 500, message = "{description.maxsize}")
    @Schema(example = "Expense for house invoice")
    private String description;

    @NotNull(message = "{categoryUid.notnull}")
    @Schema(example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID categoryUid;
}

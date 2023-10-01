package com.PBA.budgetservice.persistance.model.dtos;

import com.PBA.budgetservice.persistance.model.Repetition;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExpenseDto {
    @Schema(example = "500.32")
    private BigDecimal amount;

    @Schema(example = "House invoice")
    private String name;

    @Schema(example = "Expense for house invoice")
    private String description;

    @Schema(example = "RON")
    private String currency;

    @Schema(example = "4gf23f64-2341-2314-a4fd-5a432g33dab4")
    private UUID uid;

    @Schema(example = "cat1")
    private String categoryName;

    @Schema(example = "2023-11-08T18:30:00")
    private LocalDateTime createdAt;

    @Schema(allowableValues = { "MONTHLY", "DAILY", "NONE"}, example = "daily")
    private Repetition repetition;
}
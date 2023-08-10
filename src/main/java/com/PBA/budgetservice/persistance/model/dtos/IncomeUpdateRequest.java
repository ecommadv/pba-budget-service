package com.PBA.budgetservice.persistance.model.dtos;

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
    private BigDecimal amount;
    private String description;
    private UUID categoryUid;
}

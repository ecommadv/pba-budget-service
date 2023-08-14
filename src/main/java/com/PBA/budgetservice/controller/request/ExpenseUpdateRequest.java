package com.PBA.budgetservice.controller.request;

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
    private BigDecimal amount;
    private String name;
    private String description;
    private UUID categoryUid;
}

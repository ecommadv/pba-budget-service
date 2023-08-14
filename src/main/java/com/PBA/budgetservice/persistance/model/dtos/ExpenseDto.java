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
public class ExpenseDto {
    private BigDecimal amount;
    private String name;
    private String description;
    private String currency;
    private UUID uid;
    private String categoryName;
}
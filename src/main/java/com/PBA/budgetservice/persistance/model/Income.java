package com.PBA.budgetservice.persistance.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Income {
    private Long id;
    private BigDecimal amount;
    private String description;
    private String currency;
    private Long accountId;
    private Long categoryId;
}

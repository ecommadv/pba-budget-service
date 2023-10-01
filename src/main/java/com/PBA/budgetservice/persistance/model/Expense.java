package com.PBA.budgetservice.persistance.model;

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
public class Expense {
    private Long id;
    private BigDecimal amount;
    private String name;
    private String description;
    private String currency;
    private UUID uid;
    private Long accountId;
    private Long categoryId;
    private LocalDateTime createdAt;
    private Repetition repetition;

    public LocalDateTime getCreatedAtFloor() {
        return LocalDateTime.of(createdAt.getYear(), createdAt.getMonth(), createdAt.getDayOfMonth(), 0, 0, 0);
    }
}

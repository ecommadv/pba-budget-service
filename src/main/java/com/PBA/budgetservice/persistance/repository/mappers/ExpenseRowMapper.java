package com.PBA.budgetservice.persistance.repository.mappers;

import com.PBA.budgetservice.persistance.model.Expense;
import org.springframework.jdbc.core.RowMapper;

public interface ExpenseRowMapper extends RowMapper<Expense> {
}

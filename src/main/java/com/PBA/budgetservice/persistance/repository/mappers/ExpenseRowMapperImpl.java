package com.PBA.budgetservice.persistance.repository.mappers;

import com.PBA.budgetservice.persistance.model.Repetition;
import com.PBA.budgetservice.persistance.model.Expense;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class ExpenseRowMapperImpl implements ExpenseRowMapper {
    @Override
    public Expense mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Expense.builder()
                .id(rs.getLong("id"))
                .amount(BigDecimal.valueOf(rs.getDouble("amount")))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .currency(rs.getString("currency"))
                .uid(UUID.fromString(rs.getString("uid")))
                .accountId(rs.getLong("account_id"))
                .categoryId(rs.getLong("category_id"))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .repetition(Repetition.valueOf(rs.getString("repetition")))
                .build();
    }
}

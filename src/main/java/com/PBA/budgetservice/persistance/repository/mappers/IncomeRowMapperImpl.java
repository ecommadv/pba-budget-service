package com.PBA.budgetservice.persistance.repository.mappers;

import com.PBA.budgetservice.persistance.model.Income;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class IncomeRowMapperImpl implements IncomeRowMapper {
    @Override
    public Income mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Income.builder()
                .id(rs.getLong("id"))
                .amount(BigDecimal.valueOf(rs.getDouble("amount")))
                .description(rs.getString("description"))
                .currency(rs.getString("currency"))
                .accountId(rs.getLong("account_id"))
                .categoryId(rs.getLong("category_id"))
                .build();
    }
}

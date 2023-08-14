package com.PBA.budgetservice.persistance.repository.mappers;

import com.PBA.budgetservice.persistance.model.ExpenseCategory;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class ExpenseCategoryRowMapperImpl implements ExpenseCategoryRowMapper {
    @Override
    public ExpenseCategory mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ExpenseCategory.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .uid(UUID.fromString(rs.getString("uid")))
                .build();
    }
}

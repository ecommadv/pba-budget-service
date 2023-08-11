package com.PBA.budgetservice.persistance.repository.mappers;

import com.PBA.budgetservice.persistance.model.Account;

import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class AccountRowMapperImpl implements AccountRowMapper {
    @Override
    public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Account.builder()
                .id(rs.getInt("id"))
                .userUid(UUID.fromString(rs.getString("user_uid")))
                .currency(rs.getString("currency"))
                .build();
    }
}

package com.PBA.budgetservice.persistance.repository.mappers;

import com.PBA.budgetservice.persistance.model.Account;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Currency;
import java.util.UUID;

public interface AccountRowMapper extends RowMapper<Account> {
    public Account mapRow(ResultSet rs, int rowNum) throws SQLException;
}

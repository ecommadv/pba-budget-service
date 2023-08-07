package com.PBA.budgetservice.persistance.repository;

import com.PBA.budgetservice.persistance.repository.mappers.AccountRowMapper;
import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.repository.sql.AccountSqlProvider;
import com.PBA.budgetservice.persistance.repository.sql.SqlProvider;
import com.PBA.budgetservice.persistance.repository.JdbcRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AccountDaoImpl extends JdbcRepository<Account, Long> implements AccountDao {
    public AccountDaoImpl(AccountRowMapper rowMapper, AccountSqlProvider sqlProvider, JdbcTemplate jdbcTemplate, UtilsFactory utilsFactory) {
        super(rowMapper, sqlProvider, jdbcTemplate, utilsFactory);
    }
}

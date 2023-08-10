package com.PBA.budgetservice.persistance.repository;

import com.PBA.budgetservice.persistance.repository.mappers.AccountRowMapper;
import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.repository.sql.AccountSqlProvider;
import com.PBA.budgetservice.persistance.repository.sql.SqlProvider;
import com.PBA.budgetservice.persistance.repository.JdbcRepository;
import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class AccountDaoImpl extends JdbcRepository<Account, Long> implements AccountDao {
    private final AccountSqlProvider accountSqlProvider;
    private final JdbcTemplate jdbcTemplate;
    private final AccountRowMapper accountRowMapper;

    public AccountDaoImpl(AccountRowMapper rowMapper, AccountSqlProvider sqlProvider, JdbcTemplate jdbcTemplate, UtilsFactory utilsFactory) {
        super(rowMapper, sqlProvider, jdbcTemplate, utilsFactory);
        this.accountRowMapper = rowMapper;
        this.accountSqlProvider = sqlProvider;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Account> getByUserUidAndCurrency(Pair<UUID, String> key) {
        String sql = accountSqlProvider.selectByUserUidAndCurrency();
        return jdbcTemplate.query(sql, accountRowMapper, key.getFirst(), key.getSecond()).stream().findFirst();
    }
}

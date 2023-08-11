package com.PBA.budgetservice.persistance.repository;

import com.PBA.budgetservice.persistance.model.Income;
import com.PBA.budgetservice.persistance.repository.mappers.IncomeRowMapper;
import com.PBA.budgetservice.persistance.repository.sql.IncomeSqlProvider;
import com.PBA.budgetservice.persistance.repository.sql.SqlProvider;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class IncomeDaoImpl extends JdbcRepository<Income, Long> implements IncomeDao {
    private final IncomeRowMapper incomeRowMapper;
    private final IncomeSqlProvider incomeSqlProvider;
    private final JdbcTemplate jdbcTemplate;
    public IncomeDaoImpl(IncomeRowMapper rowMapper, IncomeSqlProvider sqlProvider, JdbcTemplate jdbcTemplate, UtilsFactory utilsFactory) {
        super(rowMapper, sqlProvider, jdbcTemplate, utilsFactory);
        this.incomeRowMapper = rowMapper;
        this.incomeSqlProvider = sqlProvider;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Income> getByUid(UUID uid) {
        String sql = incomeSqlProvider.selectByUid();
        return jdbcTemplate.query(sql, incomeRowMapper, uid).stream().findFirst();
    }
}

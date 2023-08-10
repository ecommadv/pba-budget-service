package com.PBA.budgetservice.persistance.repository;

import com.PBA.budgetservice.persistance.model.Income;
import com.PBA.budgetservice.persistance.repository.mappers.IncomeRowMapper;
import com.PBA.budgetservice.persistance.repository.sql.IncomeSqlProvider;
import com.PBA.budgetservice.persistance.repository.sql.SqlProvider;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class IncomeDaoImpl extends JdbcRepository<Income, Long> implements IncomeDao {
    public IncomeDaoImpl(IncomeRowMapper rowMapper, IncomeSqlProvider sqlProvider, JdbcTemplate jdbcTemplate, UtilsFactory utilsFactory) {
        super(rowMapper, sqlProvider, jdbcTemplate, utilsFactory);
    }
}

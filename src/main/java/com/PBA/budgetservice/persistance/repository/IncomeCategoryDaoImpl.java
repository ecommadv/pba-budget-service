package com.PBA.budgetservice.persistance.repository;

import com.PBA.budgetservice.persistance.model.IncomeCategory;
import com.PBA.budgetservice.persistance.repository.mappers.IncomeCategoryRowMapper;
import com.PBA.budgetservice.persistance.repository.sql.IncomeCategorySqlProvider;
import com.PBA.budgetservice.persistance.repository.sql.IncomeSqlProvider;
import com.PBA.budgetservice.persistance.repository.sql.SqlProvider;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class IncomeCategoryDaoImpl extends JdbcRepository<IncomeCategory, Long> implements IncomeCategoryDao {
    private final IncomeCategoryRowMapper incomeCategoryRowMapper;

    private final IncomeCategorySqlProvider incomeSqlProvider;

    private final JdbcTemplate jdbcTemplate;

    public IncomeCategoryDaoImpl(IncomeCategoryRowMapper rowMapper, IncomeCategorySqlProvider sqlProvider, JdbcTemplate jdbcTemplate, UtilsFactory utilsFactory) {
        super(rowMapper, sqlProvider, jdbcTemplate, utilsFactory);
        this.incomeCategoryRowMapper = rowMapper;
        this.incomeSqlProvider = sqlProvider;
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<IncomeCategory> getIncomeCategoryByUid(UUID uid) {
        String sql = incomeSqlProvider.selectByUid();
        return jdbcTemplate.query(sql, incomeCategoryRowMapper, uid).stream().findFirst();
    }
}

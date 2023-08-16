package com.PBA.budgetservice.persistance.repository;

import com.PBA.budgetservice.persistance.model.ExpenseCategory;
import com.PBA.budgetservice.persistance.repository.mappers.ExpenseCategoryRowMapper;
import com.PBA.budgetservice.persistance.repository.sql.ExpenseCategorySqlProvider;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class ExpenseCategoryDaoImpl extends JdbcRepository<ExpenseCategory, Long> implements ExpenseCategoryDao {
    private final ExpenseCategoryRowMapper expenseCategoryRowMapper;
    private final ExpenseCategorySqlProvider expenseCategorySqlProvider;
    private final JdbcTemplate jdbcTemplate;
    public ExpenseCategoryDaoImpl(ExpenseCategoryRowMapper rowMapper, ExpenseCategorySqlProvider sqlProvider, JdbcTemplate jdbcTemplate, UtilsFactory utilsFactory) {
        super(rowMapper, sqlProvider, jdbcTemplate, utilsFactory);
        this.expenseCategoryRowMapper = rowMapper;
        this.expenseCategorySqlProvider = sqlProvider;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<ExpenseCategory> getByUid(UUID categoryUid) {
        String sql = expenseCategorySqlProvider.selectByUid();
        return jdbcTemplate.query(sql, expenseCategoryRowMapper, categoryUid).stream().findFirst();
    }
}

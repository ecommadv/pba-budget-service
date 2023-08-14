package com.PBA.budgetservice.persistance.repository;

import com.PBA.budgetservice.persistance.model.Expense;
import com.PBA.budgetservice.persistance.repository.mappers.ExpenseRowMapper;
import com.PBA.budgetservice.persistance.repository.sql.ExpenseSqlProvider;
import com.PBA.budgetservice.persistance.repository.sql.SqlProvider;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class ExpenseDaoImpl extends JdbcRepository<Expense, Long> implements ExpenseDao {
    private final ExpenseRowMapper expenseRowMapper;
    private final ExpenseSqlProvider expenseSqlProvider;
    private final JdbcTemplate jdbcTemplate;

    public ExpenseDaoImpl(ExpenseRowMapper rowMapper, ExpenseSqlProvider sqlProvider, JdbcTemplate jdbcTemplate, UtilsFactory utilsFactory) {
        super(rowMapper, sqlProvider, jdbcTemplate, utilsFactory);
        this.expenseRowMapper = rowMapper;
        this.expenseSqlProvider = sqlProvider;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Expense> getByUid(UUID uid) {
        String sql = expenseSqlProvider.selectByUid();
        return jdbcTemplate.query(sql, expenseRowMapper, uid).stream().findFirst();
    }
}

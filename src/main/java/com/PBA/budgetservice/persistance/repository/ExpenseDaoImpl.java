package com.PBA.budgetservice.persistance.repository;

import com.PBA.budgetservice.controller.request.DateRange;
import com.PBA.budgetservice.persistance.model.Repetition;
import com.PBA.budgetservice.persistance.repository.mappers.ExpenseRowMapper;
import com.PBA.budgetservice.persistance.repository.sql.ExpenseSqlProvider;
import com.PBA.budgetservice.persistance.model.Expense;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ExpenseDaoImpl extends JdbcRepository<Expense, Long> implements ExpenseDao {
    private final ExpenseRowMapper expenseRowMapper;
    private final ExpenseSqlProvider expenseSqlProvider;
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ExpenseDaoImpl(ExpenseRowMapper rowMapper, ExpenseSqlProvider sqlProvider, JdbcTemplate jdbcTemplate, UtilsFactory utilsFactory, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(rowMapper, sqlProvider, jdbcTemplate, utilsFactory);
        this.expenseRowMapper = rowMapper;
        this.expenseSqlProvider = sqlProvider;
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Optional<Expense> getByUid(UUID uid) {
        String sql = expenseSqlProvider.selectByUid();
        return jdbcTemplate.query(sql, expenseRowMapper, uid).stream().findFirst();
    }

    @Override
    public Expense save(Expense expense) {
        Object repetitionField = expense.getRepetition().name();
        Integer repetitionIndex = 9;
        Map<Integer, Object> fieldMapping = Map.of(repetitionIndex, repetitionField);
        return super.save(expense, fieldMapping);
    }

    @Override
    public Expense update(Expense expense, Long id) {
        Object repetitionField = expense.getRepetition().name();
        Integer repetitionIndex = 9;
        Map<Integer, Object> fieldMapping = Map.of(repetitionIndex, repetitionField);
        return super.update(expense, id, fieldMapping);
    }

    @Override
    public List<Expense> getAllByUserUid(UUID userUid) {
        String sql = expenseSqlProvider.selectByUserUid();
        return jdbcTemplate.query(sql, expenseRowMapper, userUid);
    }

    @Override
    public List<Expense> getAllByFilters(UUID userUid, String name, String categoryName, String currency, DateRange dateRange) {
        String sql = expenseSqlProvider.selectByFilters();
        return namedParameterJdbcTemplate.query(
                sql,
                mapSqlParameterSource(userUid, name, categoryName, currency, dateRange),
                expenseRowMapper
        );
    }

    @Override
    public List<Expense> getByRepetition(Repetition repetition) {
        String sql = expenseSqlProvider.selectByRepetition();
        return jdbcTemplate.query(sql, expenseRowMapper, repetition.name());
    }

    private MapSqlParameterSource mapSqlParameterSource(UUID userUid, String name, String categoryName, String currency, DateRange dateRange) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("userUid", userUid, Types.OTHER);
        mapSqlParameterSource.addValue("expenseName", name, Types.VARCHAR);
        mapSqlParameterSource.addValue("categoryName", categoryName, Types.VARCHAR);
        mapSqlParameterSource.addValue("currency", currency, Types.VARCHAR);
        mapSqlParameterSource.addValue("dateAfter", dateRange.getAfter(), dateRange.getAfter() == null ? Types.DATE: Types.TIMESTAMP);
        mapSqlParameterSource.addValue("dateBefore", dateRange.getBefore(), dateRange.getBefore() == null ? Types.DATE: Types.TIMESTAMP);
        return mapSqlParameterSource;
    }
}

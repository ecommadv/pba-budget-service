package com.PBA.budgetservice.persistance.repository;

import com.PBA.budgetservice.controller.request.DateRange;
import com.PBA.budgetservice.persistance.model.Expense;
import com.PBA.budgetservice.persistance.repository.mappers.IncomeRowMapper;
import com.PBA.budgetservice.persistance.repository.sql.IncomeSqlProvider;
import com.PBA.budgetservice.persistance.model.Income;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class IncomeDaoImpl extends JdbcRepository<Income, Long> implements IncomeDao {
    private final IncomeRowMapper incomeRowMapper;
    private final IncomeSqlProvider incomeSqlProvider;
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public IncomeDaoImpl(IncomeRowMapper rowMapper, IncomeSqlProvider sqlProvider, JdbcTemplate jdbcTemplate, UtilsFactory utilsFactory, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(rowMapper, sqlProvider, jdbcTemplate, utilsFactory);
        this.incomeRowMapper = rowMapper;
        this.incomeSqlProvider = sqlProvider;
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Optional<Income> getByUid(UUID uid) {
        String sql = incomeSqlProvider.selectByUid();
        return jdbcTemplate.query(sql, incomeRowMapper, uid).stream().findFirst();
    }

    @Override
    public List<Income> getAllByUserUid(UUID userUid) {
        String sql = incomeSqlProvider.selectByUserUid();
        return jdbcTemplate.query(sql, incomeRowMapper, userUid);
    }

    @Override
    public List<Income> getAllByFilters(UUID userUid, String categoryName, String currency, DateRange dateRange) {
        String sql = incomeSqlProvider.selectByFilters();
        return namedParameterJdbcTemplate.query(
                sql,
                mapSqlParameterSource(userUid, categoryName, currency, dateRange),
                incomeRowMapper
        );
    }

    private MapSqlParameterSource mapSqlParameterSource(UUID userUid, String categoryName, String currency, DateRange dateRange) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("userUid", userUid, Types.OTHER);
        mapSqlParameterSource.addValue("categoryName", categoryName, Types.VARCHAR);
        mapSqlParameterSource.addValue("currency", currency, Types.VARCHAR);
        mapSqlParameterSource.addValue("dateAfter", dateRange.getAfter(), dateRange.getAfter() == null ? Types.DATE: Types.TIMESTAMP);
        mapSqlParameterSource.addValue("dateBefore", dateRange.getBefore(), dateRange.getBefore() == null ? Types.DATE: Types.TIMESTAMP);
        return mapSqlParameterSource;
    }
}

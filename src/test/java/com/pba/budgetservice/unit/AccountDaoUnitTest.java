package com.pba.budgetservice.unit;

import com.PBA.budgetservice.exceptions.BudgetDaoException;
import com.PBA.budgetservice.persistance.repository.UtilsFactory;
import com.PBA.budgetservice.persistance.repository.mappers.AccountRowMapper;
import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.repository.AccountDaoImpl;
import com.PBA.budgetservice.persistance.repository.sql.AccountSqlProvider;
import com.pba.budgetservice.mockgenerators.AccountMockGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

public class AccountDaoUnitTest extends BaseUnitTest {
    @InjectMocks
    private AccountDaoImpl accountDao;

    @Mock
    private AccountSqlProvider accountSqlProvider;

    @Mock
    private AccountRowMapper accountRowMapper;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private UtilsFactory utilsFactory;

    @Mock
    private KeyHolder keyHolder;

    @Test
    public void testSave() {
        // given
        Account account = AccountMockGenerator.generateMockAccount();
        account.setId(1L);
        this.setUpGetById(account);

        Map<String, Object> keys = new HashMap<>();
        keys.put("id", 1L);
        when(keyHolder.getKeys()).thenReturn(keys);
        when(utilsFactory.keyHolder()).thenReturn(keyHolder);

        // when
        Account result = accountDao.save(account);

        // then
        Assertions.assertEquals(account, result);
    }

    @Test
    public void testGetPresentById() {
        // given
        Account account = AccountMockGenerator.generateMockAccount();
        this.setUpGetById(account);

        // when
        Optional<Account> result = accountDao.getById(account.getId());

        // then
        Assertions.assertEquals(account, result.get());
    }

    @Test
    public void testGetAbsentById() {
        // given
        Long absentId = new Random().nextLong();
        this.setUpEmptyGetById(absentId);

        // when
        Optional<Account> result = accountDao.getById(absentId);

        // then
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testGetAll() {
        // given
        List<Account> accounts = AccountMockGenerator.generateMockListOfAccounts(10);
        when(accountSqlProvider.selectAll()).thenReturn("select");
        when(jdbcTemplate.query("select", accountRowMapper)).thenReturn(accounts);

        // when
        List<Account> result = accountDao.getAll();

        // then
        Assertions.assertEquals(accounts, result);
    }

    @Test
    public void testDeletePresentById() {
        // given
        Account account = AccountMockGenerator.generateMockAccount();
        when(accountSqlProvider.deleteById()).thenReturn("delete");
        when(jdbcTemplate.update("delete", account.getId())).thenReturn(1);
        this.setUpGetById(account);

        // when
        Account result = accountDao.deleteById(account.getId());

        // then
        Assertions.assertEquals(account, result);
    }

    @Test
    public void testDeleteAbsentById() {
        Long absentId = new Random().nextLong();
        this.setUpEmptyGetById(absentId);
        when(accountSqlProvider.deleteById()).thenReturn("delete");
        when(jdbcTemplate.update("delete", absentId)).thenReturn(0);

        assertThatThrownBy(() -> accountDao.deleteById(absentId))
                .isInstanceOf(BudgetDaoException.class)
                .hasMessage(String.format("Object with id %s is not stored!", absentId.toString()));
    }

    @Test
    public void testUpdatePresent() {
        // given
        Account account = AccountMockGenerator.generateMockAccount();
        Account newAccount = AccountMockGenerator.generateMockAccount();
        newAccount.setId(account.getId());
        when(accountSqlProvider.update()).thenReturn("update");
        when(jdbcTemplate.update("update", newAccount.getUserUid(), newAccount.getCurrency(), newAccount.getId())).thenReturn(1);

        // when
        Account result = accountDao.update(newAccount, newAccount.getId());

        // then
        Assertions.assertEquals(newAccount, result);
    }

    @Test
    public void testUpdateAbsent() {
        // given
        Long id = new Random().nextLong();
        Account absentAccount = AccountMockGenerator.generateMockAccount();
        absentAccount.setId(id);
        when(accountSqlProvider.update()).thenReturn("update");
        when(jdbcTemplate.update("update", absentAccount.getUserUid(), absentAccount.getCurrency(), id)).thenReturn(0);

        assertThatThrownBy(() -> accountDao.update(absentAccount, id))
                .isInstanceOf(BudgetDaoException.class)
                .hasMessage(String.format("Object with id %s is not stored!", id.toString()));
    }

    private void setUpGetById(Account account) {
        when(accountSqlProvider.selectById()).thenReturn("select");
        when(jdbcTemplate.query("select", accountRowMapper, account.getId())).thenReturn(List.of(account));
    }

    private void setUpEmptyGetById(Long id) {
        when(accountSqlProvider.selectById()).thenReturn("select");
        when(jdbcTemplate.query("select", accountRowMapper, id)).thenReturn(List.of());
    }
}

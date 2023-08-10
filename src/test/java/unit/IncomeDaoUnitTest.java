package unit;

import com.PBA.budgetservice.exceptions.BudgetDaoException;
import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.Income;
import com.PBA.budgetservice.persistance.model.IncomeCategory;
import com.PBA.budgetservice.persistance.repository.IncomeDaoImpl;
import com.PBA.budgetservice.persistance.repository.UtilsFactory;
import com.PBA.budgetservice.persistance.repository.mappers.IncomeRowMapper;
import com.PBA.budgetservice.persistance.repository.sql.IncomeSqlProvider;
import mockgenerators.AccountMockGenerator;
import mockgenerators.IncomeCategoryMockGenerator;
import mockgenerators.IncomeMockGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IncomeDaoUnitTest {
    @InjectMocks
    private IncomeDaoImpl incomeDao;

    @Mock
    private IncomeSqlProvider incomeSqlProvider;

    @Mock
    private IncomeRowMapper incomeRowMapper;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private UtilsFactory utilsFactory;

    @Mock
    private KeyHolder keyHolder;

    @Test
    public void testSave() {
        // given
        List<Account> accounts = AccountMockGenerator.generateMockListOfAccounts(3);
        List<IncomeCategory> incomeCategories = IncomeCategoryMockGenerator.generateMockListOfIncomeCategories(3);
        Income income = IncomeMockGenerator.generateMockIncome(incomeCategories, accounts);
        income.setId(1L);
        this.setUpGetById(income);

        Map<String, Object> keys = new HashMap<>();
        keys.put("id", 1L);
        when(keyHolder.getKeys()).thenReturn(keys);
        when(utilsFactory.keyHolder()).thenReturn(keyHolder);

        // when
        Income result = incomeDao.save(income);

        // then
        Assertions.assertEquals(income, result);
    }

    @Test
    public void testGetPresentById() {
        // given
        List<Account> accounts = AccountMockGenerator.generateMockListOfAccounts(3);
        List<IncomeCategory> incomeCategories = IncomeCategoryMockGenerator.generateMockListOfIncomeCategories(3);
        Income income = IncomeMockGenerator.generateMockIncome(incomeCategories, accounts);
        this.setUpGetById(income);

        // when
        Optional<Income> result = incomeDao.getById(income.getId());

        // then
        Assertions.assertEquals(income, result.get());
    }

    @Test
    public void testGetAbsentById() {
        // given
        Long absentId = new Random().nextLong();
        this.setUpEmptyGetById(absentId);

        // when
        Optional<Income> result = incomeDao.getById(absentId);

        // then
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testGetAll() {
        // given
        List<Account> accounts = AccountMockGenerator.generateMockListOfAccounts(3);
        List<IncomeCategory> incomeCategories = IncomeCategoryMockGenerator.generateMockListOfIncomeCategories(3);
        List<Income> incomes = IncomeMockGenerator.generateMockListOfIncomes(incomeCategories, accounts, 10);
        when(incomeSqlProvider.selectAll()).thenReturn("select");
        when(jdbcTemplate.query("select", incomeRowMapper)).thenReturn(incomes);

        // when
        List<Income> result = incomeDao.getAll();

        // then
        Assertions.assertEquals(incomes, result);
    }

    @Test
    public void testDeletePresentById() {
        // given
        List<Account> accounts = AccountMockGenerator.generateMockListOfAccounts(3);
        List<IncomeCategory> incomeCategories = IncomeCategoryMockGenerator.generateMockListOfIncomeCategories(3);
        Income income = IncomeMockGenerator.generateMockIncome(incomeCategories, accounts);
        when(incomeSqlProvider.deleteById()).thenReturn("delete");
        when(jdbcTemplate.update("delete", income.getId())).thenReturn(1);
        this.setUpGetById(income);

        // when
        Income result = incomeDao.deleteById(income.getId());

        // then
        Assertions.assertEquals(income, result);
    }

    @Test
    public void testDeleteAbsentById() {
        Long absentId = new Random().nextLong();
        this.setUpEmptyGetById(absentId);
        when(incomeSqlProvider.deleteById()).thenReturn("delete");
        when(jdbcTemplate.update("delete", absentId)).thenReturn(0);

        assertThatThrownBy(() -> incomeDao.deleteById(absentId))
                .isInstanceOf(BudgetDaoException.class)
                .hasMessage(String.format("Object with id %s is not stored!", absentId.toString()));
    }

    @Test
    public void testUpdatePresent() {
        // given
        List<Account> accounts = AccountMockGenerator.generateMockListOfAccounts(3);
        List<IncomeCategory> incomeCategories = IncomeCategoryMockGenerator.generateMockListOfIncomeCategories(3);
        Income income = IncomeMockGenerator.generateMockIncome(incomeCategories, accounts);
        Income newIncome = IncomeMockGenerator.generateMockIncome(incomeCategories, accounts);
        newIncome.setId(income.getId());
        when(incomeSqlProvider.update()).thenReturn("update");
        when(jdbcTemplate.update("update",
                newIncome.getAmount(),
                newIncome.getDescription(),
                newIncome.getCurrency(),
                newIncome.getUid(),
                newIncome.getAccountId(),
                newIncome.getCategoryId(),
                newIncome.getId())).thenReturn(1);

        // when
        Income result = incomeDao.update(newIncome, newIncome.getId());

        // then
        Assertions.assertEquals(newIncome, result);
    }

    @Test
    public void testUpdateAbsent() {
        // given
        Long id = new Random().nextLong();
        List<Account> accounts = AccountMockGenerator.generateMockListOfAccounts(3);
        List<IncomeCategory> incomeCategories = IncomeCategoryMockGenerator.generateMockListOfIncomeCategories(3);
        Income absentIncome = IncomeMockGenerator.generateMockIncome(incomeCategories, accounts);
        absentIncome.setId(id);
        when(incomeSqlProvider.update()).thenReturn("update");
        when(jdbcTemplate.update("update",
                absentIncome.getAmount(),
                absentIncome.getDescription(),
                absentIncome.getCurrency(),
                absentIncome.getUid(),
                absentIncome.getAccountId(),
                absentIncome.getCategoryId(),
                absentIncome.getId())).thenReturn(0);

        assertThatThrownBy(() -> incomeDao.update(absentIncome, id))
                .isInstanceOf(BudgetDaoException.class)
                .hasMessage(String.format("Object with id %s is not stored!", id.toString()));
    }

    private void setUpGetById(Income income) {
        when(incomeSqlProvider.selectById()).thenReturn("select");
        when(jdbcTemplate.query("select", incomeRowMapper, income.getId())).thenReturn(List.of(income));
    }

    private void setUpEmptyGetById(Long id) {
        when(incomeSqlProvider.selectById()).thenReturn("select");
        when(jdbcTemplate.query("select", incomeRowMapper, id)).thenReturn(List.of());
    }
}

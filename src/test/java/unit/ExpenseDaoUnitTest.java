package unit;

import com.PBA.budgetservice.exceptions.BudgetDaoException;
import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.Expense;
import com.PBA.budgetservice.persistance.model.ExpenseCategory;
import com.PBA.budgetservice.persistance.repository.ExpenseDaoImpl;
import com.PBA.budgetservice.persistance.repository.IncomeDaoImpl;
import com.PBA.budgetservice.persistance.repository.UtilsFactory;
import com.PBA.budgetservice.persistance.repository.mappers.ExpenseRowMapper;
import com.PBA.budgetservice.persistance.repository.sql.ExpenseSqlProvider;
import mockgenerators.AccountMockGenerator;
import mockgenerators.ExpenseCategoryMockGenerator;
import mockgenerators.ExpenseMockGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.support.KeyHolder;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExpenseDaoUnitTest {
    @InjectMocks
    private ExpenseDaoImpl expenseDao;

    @Mock
    private ExpenseSqlProvider expenseSqlProvider;

    @Mock
    private ExpenseRowMapper expenseRowMapper;

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
        List<ExpenseCategory> expenseCategories = ExpenseCategoryMockGenerator.generateMockListOfExpenseCategories(3);
        Expense expense = ExpenseMockGenerator.generateMockExpense(expenseCategories, accounts);
        expense.setId(1L);
        this.setUpGetById(expense);

        Map<String, Object> keys = new HashMap<>();
        keys.put("id", 1L);
        when(keyHolder.getKeys()).thenReturn(keys);
        when(utilsFactory.keyHolder()).thenReturn(keyHolder);

        // when
        Expense result = expenseDao.save(expense);

        // then
        Assertions.assertEquals(expense, result);
    }

    @Test
    public void testGetPresentById() {
        // given
        List<Account> accounts = AccountMockGenerator.generateMockListOfAccounts(3);
        List<ExpenseCategory> expenseCategories = ExpenseCategoryMockGenerator.generateMockListOfExpenseCategories(3);
        Expense expense = ExpenseMockGenerator.generateMockExpense(expenseCategories, accounts);
        this.setUpGetById(expense);

        // when
        Optional<Expense> result = expenseDao.getById(expense.getId());

        // then
        Assertions.assertEquals(expense, result.get());
    }

    @Test
    public void testGetAbsentById() {
        // given
        Long absentId = new Random().nextLong();
        this.setUpEmptyGetById(absentId);

        // when
        Optional<Expense> result = expenseDao.getById(absentId);

        // then
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testGetAll() {
        // given
        List<Account> accounts = AccountMockGenerator.generateMockListOfAccounts(3);
        List<ExpenseCategory> expenseCategories = ExpenseCategoryMockGenerator.generateMockListOfExpenseCategories(3);
        List<Expense> expenses = ExpenseMockGenerator.generateMockListOfExpenses(expenseCategories, accounts, 10);
        when(expenseSqlProvider.selectAll()).thenReturn("select");
        when(jdbcTemplate.query("select", expenseRowMapper)).thenReturn(expenses);

        // when
        List<Expense> result = expenseDao.getAll();

        // then
        Assertions.assertEquals(expenses, result);
    }

    @Test
    public void testDeletePresentById() {
        // given
        List<Account> accounts = AccountMockGenerator.generateMockListOfAccounts(3);
        List<ExpenseCategory> expenseCategories = ExpenseCategoryMockGenerator.generateMockListOfExpenseCategories(3);
        Expense expense = ExpenseMockGenerator.generateMockExpense(expenseCategories, accounts);
        when(expenseSqlProvider.deleteById()).thenReturn("delete");
        when(jdbcTemplate.update("delete", expense.getId())).thenReturn(1);
        this.setUpGetById(expense);

        // when
        Expense result = expenseDao.deleteById(expense.getId());

        // then
        Assertions.assertEquals(expense, result);
    }

    @Test
    public void testDeleteAbsentById() {
        Long absentId = new Random().nextLong();
        this.setUpEmptyGetById(absentId);
        when(expenseSqlProvider.deleteById()).thenReturn("delete");
        when(jdbcTemplate.update("delete", absentId)).thenReturn(0);

        assertThatThrownBy(() -> expenseDao.deleteById(absentId))
                .isInstanceOf(BudgetDaoException.class)
                .hasMessage(String.format("Object with id %s is not stored!", absentId.toString()));
    }

    @Test
    public void testUpdatePresent() {
        // given
        List<Account> accounts = AccountMockGenerator.generateMockListOfAccounts(3);
        List<ExpenseCategory> expenseCategories = ExpenseCategoryMockGenerator.generateMockListOfExpenseCategories(3);
        Expense expense = ExpenseMockGenerator.generateMockExpense(expenseCategories, accounts);
        Expense newExpense = ExpenseMockGenerator.generateMockExpense(expenseCategories, accounts);
        newExpense.setId(expense.getId());
        when(expenseSqlProvider.update()).thenReturn("update");
        when(jdbcTemplate.update("update",
                newExpense.getAmount(),
                newExpense.getName(),
                newExpense.getDescription(),
                newExpense.getCurrency(),
                newExpense.getUid(),
                newExpense.getAccountId(),
                newExpense.getCategoryId(),
                newExpense.getId())).thenReturn(1);

        // when
        Expense result = expenseDao.update(newExpense, newExpense.getId());

        // then
        Assertions.assertEquals(newExpense, result);
    }

    @Test
    public void testUpdateAbsent() {
        // given
        Long id = new Random().nextLong();
        List<Account> accounts = AccountMockGenerator.generateMockListOfAccounts(3);
        List<ExpenseCategory> expenseCategories = ExpenseCategoryMockGenerator.generateMockListOfExpenseCategories(3);
        Expense absentExpense = ExpenseMockGenerator.generateMockExpense(expenseCategories, accounts);
        absentExpense.setId(id);
        when(expenseSqlProvider.update()).thenReturn("update");
        when(jdbcTemplate.update("update",
                absentExpense.getAmount(),
                absentExpense.getName(),
                absentExpense.getDescription(),
                absentExpense.getCurrency(),
                absentExpense.getUid(),
                absentExpense.getAccountId(),
                absentExpense.getCategoryId(),
                absentExpense.getId())).thenReturn(0);

        assertThatThrownBy(() -> expenseDao.update(absentExpense, id))
                .isInstanceOf(BudgetDaoException.class)
                .hasMessage(String.format("Object with id %s is not stored!", id.toString()));
    }

    private void setUpGetById(Expense expense) {
        when(expenseSqlProvider.selectById()).thenReturn("select");
        when(jdbcTemplate.query("select", expenseRowMapper, expense.getId())).thenReturn(List.of(expense));
    }

    private void setUpEmptyGetById(Long id) {
        when(expenseSqlProvider.selectById()).thenReturn("select");
        when(jdbcTemplate.query("select", expenseRowMapper, id)).thenReturn(List.of());
    }
}

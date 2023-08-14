package unit;

import com.PBA.budgetservice.exceptions.BudgetDaoException;
import com.PBA.budgetservice.persistance.model.Expense;
import com.PBA.budgetservice.persistance.model.ExpenseCategory;
import com.PBA.budgetservice.persistance.repository.ExpenseCategoryDaoImpl;
import com.PBA.budgetservice.persistance.repository.UtilsFactory;
import com.PBA.budgetservice.persistance.repository.mappers.ExpenseCategoryRowMapper;
import com.PBA.budgetservice.persistance.repository.sql.ExpenseCategorySqlProvider;
import mockgenerators.ExpenseCategoryMockGenerator;
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
public class ExpenseCategoryDaoUnitTest {
    @InjectMocks
    private ExpenseCategoryDaoImpl expenseCategoryDao;

    @Mock
    private ExpenseCategorySqlProvider expenseCategorySqlProvider;

    @Mock
    private ExpenseCategoryRowMapper expenseCategoryRowMapper;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private UtilsFactory utilsFactory;

    @Mock
    private KeyHolder keyHolder;

    @Test
    public void testSave() {
        // given
        ExpenseCategory expenseCategory = ExpenseCategoryMockGenerator.generateMockExpenseCategory();
        expenseCategory.setId(1L);
        this.setUpGetById(expenseCategory);

        Map<String, Object> keys = new HashMap<>();
        keys.put("id", 1L);
        when(keyHolder.getKeys()).thenReturn(keys);
        when(utilsFactory.keyHolder()).thenReturn(keyHolder);

        // when
        ExpenseCategory result = expenseCategoryDao.save(expenseCategory);

        // then
        Assertions.assertEquals(expenseCategory, result);
    }

    @Test
    public void testGetPresentById() {
        // given
        ExpenseCategory expenseCategory = ExpenseCategoryMockGenerator.generateMockExpenseCategory();
        this.setUpGetById(expenseCategory);

        // when
        Optional<ExpenseCategory> result = expenseCategoryDao.getById(expenseCategory.getId());

        // then
        Assertions.assertEquals(expenseCategory, result.get());
    }

    @Test
    public void testGetAbsentById() {
        // given
        Long absentId = new Random().nextLong();
        this.setUpEmptyGetById(absentId);

        // when
        Optional<ExpenseCategory> result = expenseCategoryDao.getById(absentId);

        // then
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testGetAll() {
        // given
        List<ExpenseCategory> expenseCategories = ExpenseCategoryMockGenerator.generateMockListOfExpenseCategories(10);
        when(expenseCategorySqlProvider.selectAll()).thenReturn("select");
        when(jdbcTemplate.query("select", expenseCategoryRowMapper)).thenReturn(expenseCategories);

        // when
        List<ExpenseCategory> result = expenseCategoryDao.getAll();

        // then
        Assertions.assertEquals(expenseCategories, result);
    }

    @Test
    public void testDeletePresentById() {
        // given
        ExpenseCategory expenseCategory = ExpenseCategoryMockGenerator.generateMockExpenseCategory();
        when(expenseCategorySqlProvider.deleteById()).thenReturn("delete");
        when(jdbcTemplate.update("delete", expenseCategory.getId())).thenReturn(1);
        this.setUpGetById(expenseCategory);

        // when
        ExpenseCategory result = expenseCategoryDao.deleteById(expenseCategory.getId());

        // then
        Assertions.assertEquals(expenseCategory, result);
    }

    @Test
    public void testDeleteAbsentById() {
        Long absentId = new Random().nextLong();
        this.setUpEmptyGetById(absentId);
        when(expenseCategorySqlProvider.deleteById()).thenReturn("delete");
        when(jdbcTemplate.update("delete", absentId)).thenReturn(0);

        assertThatThrownBy(() -> expenseCategoryDao.deleteById(absentId))
                .isInstanceOf(BudgetDaoException.class)
                .hasMessage(String.format("Object with id %s is not stored!", absentId.toString()));
    }

    @Test
    public void testUpdatePresent() {
        // given
        ExpenseCategory expenseCategory = ExpenseCategoryMockGenerator.generateMockExpenseCategory();
        ExpenseCategory newExpenseCategory = ExpenseCategoryMockGenerator.generateMockExpenseCategory();
        newExpenseCategory.setId(expenseCategory.getId());
        when(expenseCategorySqlProvider.update()).thenReturn("update");
        when(jdbcTemplate.update("update", newExpenseCategory.getName(), newExpenseCategory.getUid(), newExpenseCategory.getId())).thenReturn(1);

        // when
        ExpenseCategory result = expenseCategoryDao.update(newExpenseCategory, newExpenseCategory.getId());

        // then
        Assertions.assertEquals(newExpenseCategory, result);
    }

    @Test
    public void testUpdateAbsent() {
        // given
        Long id = new Random().nextLong();
        ExpenseCategory absentExpenseCategory = ExpenseCategoryMockGenerator.generateMockExpenseCategory();
        absentExpenseCategory.setId(id);
        when(expenseCategorySqlProvider.update()).thenReturn("update");
        when(jdbcTemplate.update("update", absentExpenseCategory.getName(), absentExpenseCategory.getUid(), id)).thenReturn(0);

        assertThatThrownBy(() -> expenseCategoryDao.update(absentExpenseCategory, id))
                .isInstanceOf(BudgetDaoException.class)
                .hasMessage(String.format("Object with id %s is not stored!", id.toString()));
    }

    private void setUpGetById(ExpenseCategory expenseCategory) {
        when(expenseCategorySqlProvider.selectById()).thenReturn("select");
        when(jdbcTemplate.query("select", expenseCategoryRowMapper, expenseCategory.getId())).thenReturn(List.of(expenseCategory));
    }

    private void setUpEmptyGetById(Long id) {
        when(expenseCategorySqlProvider.selectById()).thenReturn("select");
        when(jdbcTemplate.query("select", expenseCategoryRowMapper, id)).thenReturn(List.of());
    }
}

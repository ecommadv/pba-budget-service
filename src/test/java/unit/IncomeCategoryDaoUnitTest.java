package unit;

import com.PBA.budgetservice.exceptions.BudgetDaoException;
import com.PBA.budgetservice.persistance.model.IncomeCategory;
import com.PBA.budgetservice.persistance.repository.IncomeCategoryDaoImpl;
import com.PBA.budgetservice.persistance.repository.UtilsFactory;
import com.PBA.budgetservice.persistance.repository.mappers.IncomeCategoryRowMapper;
import com.PBA.budgetservice.persistance.repository.sql.IncomeCategorySqlProvider;
import mockgenerators.IncomeCategoryMockGenerator;
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
public class IncomeCategoryDaoUnitTest {
    @InjectMocks
    private IncomeCategoryDaoImpl incomeCategoryDao;

    @Mock
    private IncomeCategorySqlProvider incomeCategorySqlProvider;

    @Mock
    private IncomeCategoryRowMapper incomeCategoryRowMapper;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private UtilsFactory utilsFactory;

    @Mock
    private KeyHolder keyHolder;

    @Test
    public void testSave() {
        // given
        IncomeCategory incomeCategory = IncomeCategoryMockGenerator.generateMockIncomeCategory();
        incomeCategory.setId(1L);
        this.setUpGetById(incomeCategory);

        Map<String, Object> keys = new HashMap<>();
        keys.put("id", 1L);
        when(keyHolder.getKeys()).thenReturn(keys);
        when(utilsFactory.keyHolder()).thenReturn(keyHolder);

        // when
        IncomeCategory result = incomeCategoryDao.save(incomeCategory);

        // then
        Assertions.assertEquals(incomeCategory, result);
    }

    @Test
    public void testGetPresentById() {
        // given
        IncomeCategory incomeCategory = IncomeCategoryMockGenerator.generateMockIncomeCategory();
        this.setUpGetById(incomeCategory);

        // when
        Optional<IncomeCategory> result = incomeCategoryDao.getById(incomeCategory.getId());

        // then
        Assertions.assertEquals(incomeCategory, result.get());
    }

    @Test
    public void testGetAbsentById() {
        // given
        Long absentId = new Random().nextLong();
        this.setUpEmptyGetById(absentId);

        // when
        Optional<IncomeCategory> result = incomeCategoryDao.getById(absentId);

        // then
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testGetAll() {
        // given
        List<IncomeCategory> incomeCategories = IncomeCategoryMockGenerator.generateMockListOfIncomeCategories(10);
        when(incomeCategorySqlProvider.selectAll()).thenReturn("select");
        when(jdbcTemplate.query("select", incomeCategoryRowMapper)).thenReturn(incomeCategories);

        // when
        List<IncomeCategory> result = incomeCategoryDao.getAll();

        // then
        Assertions.assertEquals(incomeCategories, result);
    }

    @Test
    public void testDeletePresentById() {
        // given
        IncomeCategory incomeCategory = IncomeCategoryMockGenerator.generateMockIncomeCategory();
        when(incomeCategorySqlProvider.deleteById()).thenReturn("delete");
        when(jdbcTemplate.update("delete", incomeCategory.getId())).thenReturn(1);
        this.setUpGetById(incomeCategory);

        // when
        IncomeCategory result = incomeCategoryDao.deleteById(incomeCategory.getId());

        // then
        Assertions.assertEquals(incomeCategory, result);
    }

    @Test
    public void testDeleteAbsentById() {
        Long absentId = new Random().nextLong();
        this.setUpEmptyGetById(absentId);
        when(incomeCategorySqlProvider.deleteById()).thenReturn("delete");
        when(jdbcTemplate.update("delete", absentId)).thenReturn(0);

        assertThatThrownBy(() -> incomeCategoryDao.deleteById(absentId))
                .isInstanceOf(BudgetDaoException.class)
                .hasMessage(String.format("Object with id %s is not stored!", absentId.toString()));
    }

    @Test
    public void testUpdatePresent() {
        // given
        IncomeCategory incomeCategory = IncomeCategoryMockGenerator.generateMockIncomeCategory();
        IncomeCategory newIncomeCategory = IncomeCategoryMockGenerator.generateMockIncomeCategory();
        newIncomeCategory.setId(incomeCategory.getId());
        when(incomeCategorySqlProvider.update()).thenReturn("update");
        when(jdbcTemplate.update("update", newIncomeCategory.getName(), newIncomeCategory.getUid(), newIncomeCategory.getId())).thenReturn(1);

        // when
        IncomeCategory result = incomeCategoryDao.update(newIncomeCategory, newIncomeCategory.getId());

        // then
        Assertions.assertEquals(newIncomeCategory, result);
    }

    @Test
    public void testUpdateAbsent() {
        // given
        Long id = new Random().nextLong();
        IncomeCategory absentIncomeCategory = IncomeCategoryMockGenerator.generateMockIncomeCategory();
        absentIncomeCategory.setId(id);
        when(incomeCategorySqlProvider.update()).thenReturn("update");
        when(jdbcTemplate.update("update", absentIncomeCategory.getName(), absentIncomeCategory.getUid(), id)).thenReturn(0);

        assertThatThrownBy(() -> incomeCategoryDao.update(absentIncomeCategory, id))
                .isInstanceOf(BudgetDaoException.class)
                .hasMessage(String.format("Object with id %s is not stored!", id.toString()));
    }

    private void setUpGetById(IncomeCategory incomeCategory) {
        when(incomeCategorySqlProvider.selectById()).thenReturn("select");
        when(jdbcTemplate.query("select", incomeCategoryRowMapper, incomeCategory.getId())).thenReturn(List.of(incomeCategory));
    }

    private void setUpEmptyGetById(Long id) {
        when(incomeCategorySqlProvider.selectById()).thenReturn("select");
        when(jdbcTemplate.query("select", incomeCategoryRowMapper, id)).thenReturn(List.of());
    }
}

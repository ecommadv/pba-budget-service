package unit;

import com.PBA.budgetservice.exceptions.BudgetServiceException;
import com.PBA.budgetservice.persistance.model.Expense;
import com.PBA.budgetservice.persistance.model.ExpenseCategory;
import com.PBA.budgetservice.persistance.repository.ExpenseCategoryDao;
import com.PBA.budgetservice.service.ExpenseCategoryServiceImpl;
import mockgenerators.ExpenseCategoryMockGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExpenseCategoryServiceUnitTest {
    @InjectMocks
    private ExpenseCategoryServiceImpl expenseCategoryService;

    @Mock
    private ExpenseCategoryDao expenseCategoryDao;

    @Test
    public void testGetPresentExpenseCategoryByUid() {
        ExpenseCategory expenseCategory = ExpenseCategoryMockGenerator.generateMockExpenseCategory();
        when(expenseCategoryDao.getByUid(expenseCategory.getUid())).thenReturn(Optional.of(expenseCategory));

        ExpenseCategory result = expenseCategoryService.getByUid(expenseCategory.getUid());

        Assertions.assertEquals(expenseCategory, result);
    }

    @Test
    public void testGetAbsentExpenseCategoryByUid() {
        ExpenseCategory expenseCategory = ExpenseCategoryMockGenerator.generateMockExpenseCategory();
        when(expenseCategoryDao.getByUid(expenseCategory.getUid())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> expenseCategoryService.getByUid(expenseCategory.getUid()))
                .isInstanceOf(BudgetServiceException.class)
                .hasMessage(String.format("Expense category with uid %s does not exist!", expenseCategory.getUid().toString()));
    }
}

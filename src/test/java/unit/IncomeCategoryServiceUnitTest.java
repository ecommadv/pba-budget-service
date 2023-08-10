package unit;

import com.PBA.budgetservice.exceptions.BudgetServiceException;
import com.PBA.budgetservice.persistance.model.IncomeCategory;
import com.PBA.budgetservice.persistance.repository.IncomeCategoryDao;
import com.PBA.budgetservice.persistance.repository.IncomeCategoryDaoImpl;
import com.PBA.budgetservice.service.IncomeCategoryService;
import com.PBA.budgetservice.service.IncomeCategoryServiceImpl;
import mockgenerators.IncomeCategoryMockGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IncomeCategoryServiceUnitTest {
    @InjectMocks
    private IncomeCategoryServiceImpl incomeCategoryService;

    @Mock
    private IncomeCategoryDao incomeCategoryDao;

    @Test
    public void testGetPresentIncomeCategoryByUid() {
        IncomeCategory incomeCategory = IncomeCategoryMockGenerator.generateMockIncomeCategory();
        when(incomeCategoryDao.getIncomeCategoryByUid(incomeCategory.getUid())).thenReturn(Optional.of(incomeCategory));

        IncomeCategory result = incomeCategoryService.getIncomeCategoryByUid(incomeCategory.getUid());

        Assertions.assertEquals(incomeCategory, result);
    }

    @Test
    public void testGetAbsentIncomeCategoryByUid() {
        IncomeCategory incomeCategory = IncomeCategoryMockGenerator.generateMockIncomeCategory();
        when(incomeCategoryDao.getIncomeCategoryByUid(incomeCategory.getUid())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> incomeCategoryService.getIncomeCategoryByUid(incomeCategory.getUid()))
                .isInstanceOf(BudgetServiceException.class)
                .hasMessage(String.format("Income category with uid %s does not exist!", incomeCategory.getUid().toString()));
    }
}

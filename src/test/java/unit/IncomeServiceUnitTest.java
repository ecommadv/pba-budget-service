package unit;

import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.Income;
import com.PBA.budgetservice.persistance.model.IncomeCategory;
import com.PBA.budgetservice.persistance.repository.IncomeDao;
import com.PBA.budgetservice.service.IncomeService;
import com.PBA.budgetservice.service.IncomeServiceImpl;
import mockgenerators.AccountMockGenerator;
import mockgenerators.IncomeCategoryMockGenerator;
import mockgenerators.IncomeMockGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class IncomeServiceUnitTest {
    @InjectMocks
    private IncomeServiceImpl incomeService;
    @Mock
    private IncomeDao incomeDao;

    @Test
    public void testAdd() {
        // given
        List<IncomeCategory> incomeCategoryList = IncomeCategoryMockGenerator.generateMockListOfIncomeCategories(3);
        List<Account> accounts = AccountMockGenerator.generateMockListOfAccounts(3);
        Income income = IncomeMockGenerator.generateMockIncome(incomeCategoryList, accounts);
        when(incomeDao.save(income)).thenReturn(income);

        // when
        Income result = incomeService.addIncome(income);

        // then
        Assertions.assertEquals(income, result);
    }
}

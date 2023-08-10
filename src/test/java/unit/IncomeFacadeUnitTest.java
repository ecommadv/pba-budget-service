package unit;

import com.PBA.budgetservice.facade.IncomeFacadeImpl;
import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.Income;
import com.PBA.budgetservice.persistance.model.IncomeCategory;
import com.PBA.budgetservice.persistance.model.dtos.IncomeDto;
import com.PBA.budgetservice.persistance.model.dtos.IncomeDtoMapper;
import com.PBA.budgetservice.persistance.model.dtos.IncomeRequest;
import com.PBA.budgetservice.service.AccountService;
import com.PBA.budgetservice.service.IncomeCategoryService;
import com.PBA.budgetservice.service.IncomeService;
import mockgenerators.AccountMockGenerator;
import mockgenerators.IncomeCategoryMockGenerator;
import mockgenerators.IncomeMockGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class IncomeFacadeUnitTest {
    @InjectMocks
    private IncomeFacadeImpl incomeFacade;

    @Mock
    private IncomeService incomeService;

    @Mock
    private IncomeDtoMapper incomeDtoMapper;

    @Mock
    private AccountService accountService;

    @Mock
    private IncomeCategoryService incomeCategoryService;

    @Test
    public void testAddIncomeRequest() {
        // given
        List<Account> accountList = AccountMockGenerator.generateMockListOfAccounts(3);
        List<IncomeCategory> incomeCategories = IncomeCategoryMockGenerator.generateMockListOfIncomeCategories(3);
        IncomeRequest incomeRequest = IncomeMockGenerator.generateMockIncomeRequest(incomeCategories, accountList);
        Income income = IncomeMockGenerator.generateMockIncome(incomeCategories, accountList);
        Account account = AccountMockGenerator.generateMockAccount();
        IncomeCategory incomeCategory = IncomeCategoryMockGenerator.generateMockIncomeCategory();
        IncomeDto incomeResponse = IncomeDto.builder().build();
        when(incomeDtoMapper.toIncome(incomeRequest)).thenReturn(income);
        when(incomeDtoMapper.toAccount(incomeRequest)).thenReturn(account);
        when(accountService.addAccount(account)).thenReturn(account);
        when(incomeCategoryService.getIncomeCategoryByUid(incomeRequest.getCategoryUid())).thenReturn(incomeCategory);
        when(incomeService.addIncome(income)).thenReturn(income);
        when(incomeDtoMapper.toIncomeResponse(income, incomeCategory.getName())).thenReturn(incomeResponse);

        // when
        IncomeDto result = incomeFacade.addIncome(incomeRequest);

        // then
        Assertions.assertEquals(incomeResponse, result);
    }
}
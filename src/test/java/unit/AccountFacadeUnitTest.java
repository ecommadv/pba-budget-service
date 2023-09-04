package unit;

import com.PBA.budgetservice.controller.request.AccountCreateRequest;
import com.PBA.budgetservice.facade.AccountFacadeImpl;
import com.PBA.budgetservice.mapper.AccountMapper;
import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.dtos.AccountDto;
import com.PBA.budgetservice.service.AccountService;
import mockgenerators.AccountMockGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class AccountFacadeUnitTest {
    @InjectMocks
    private AccountFacadeImpl accountFacade;

    @Mock
    private AccountService accountService;

    @Mock
    private AccountMapper accountMapper;

    @Test
    public void testAddIncomeRequest() {
        // given
        AccountCreateRequest accountCreateRequest = AccountMockGenerator.generateMockAccountCreateRequest();
        Account account = AccountMockGenerator.generateMockAccount();
        AccountDto accountDto = AccountMockGenerator.generateMockAccountDto();
        when(accountMapper.toAccount(accountCreateRequest)).thenReturn(account);
        when(accountService.addAccount(account)).thenReturn(account);
        when(accountMapper.toAccountDto(account)).thenReturn(accountDto);

        // when
        AccountDto result = accountFacade.createAccount(accountCreateRequest);

        // then
        assertEquals(result, accountDto);
    }
}
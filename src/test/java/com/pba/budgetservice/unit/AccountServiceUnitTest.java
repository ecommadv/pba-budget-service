package com.pba.budgetservice.unit;

import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.repository.AccountDao;
import com.PBA.budgetservice.service.AccountServiceImpl;
import com.pba.budgetservice.mockgenerators.AccountMockGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.when;
public class AccountServiceUnitTest extends BaseUnitTest {
    @InjectMocks
    private AccountServiceImpl accountService;
    @Mock
    private AccountDao accountDao;

    @Test
    public void testAdd() {
        // given
        Account account = AccountMockGenerator.generateMockAccount();
        when(accountDao.save(account)).thenReturn(account);

        // when
        Account result = accountService.addAccount(account);

        // then
        Assertions.assertEquals(account, result);
    }
}

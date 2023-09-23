package com.pba.budgetservice.unit;

import com.PBA.budgetservice.facade.IncomeFacadeImpl;
import com.PBA.budgetservice.mapper.IncomeMapper;
import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.Income;
import com.PBA.budgetservice.persistance.model.IncomeCategory;
import com.PBA.budgetservice.persistance.model.dtos.IncomeDto;
import com.PBA.budgetservice.controller.request.IncomeCreateRequest;
import com.PBA.budgetservice.security.JwtSecurityService;
import com.PBA.budgetservice.service.AccountService;
import com.PBA.budgetservice.service.CurrencyService;
import com.PBA.budgetservice.service.IncomeCategoryService;
import com.PBA.budgetservice.service.IncomeService;
import com.pba.budgetservice.mockgenerators.AccountMockGenerator;
import com.pba.budgetservice.mockgenerators.IncomeCategoryMockGenerator;
import com.pba.budgetservice.mockgenerators.IncomeMockGenerator;
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
    private IncomeMapper incomeDtoMapper;

    @Mock
    private AccountService accountService;

    @Mock
    private IncomeCategoryService incomeCategoryService;

    @Mock
    private CurrencyService currencyService;

    @Mock
    private JwtSecurityService jwtSecurityService;

    @Test
    public void testAddIncomeRequest() {
        // given
        List<Account> accountList = AccountMockGenerator.generateMockListOfAccounts(3);
        List<IncomeCategory> incomeCategories = IncomeCategoryMockGenerator.generateMockListOfIncomeCategories(3);
        IncomeCreateRequest incomeRequest = IncomeMockGenerator.generateMockIncomeCreateRequest(incomeCategories);
        Income income = IncomeMockGenerator.generateMockIncome(incomeCategories, accountList);
        Account account = AccountMockGenerator.generateMockAccount();
        IncomeCategory incomeCategory = IncomeCategoryMockGenerator.generateMockIncomeCategory();
        IncomeDto incomeResponse = IncomeDto.builder().build();
        when(incomeDtoMapper.toIncome(incomeRequest)).thenReturn(income);
        when(incomeDtoMapper.toAccount(incomeRequest, account.getUserUid())).thenReturn(account);
        when(accountService.addAccount(account)).thenReturn(account);
        when(incomeCategoryService.getIncomeCategoryByUid(incomeRequest.getCategoryUid())).thenReturn(incomeCategory);
        when(incomeService.addIncome(income)).thenReturn(income);
        when(incomeDtoMapper.toIncomeDto(income, incomeCategory.getName())).thenReturn(incomeResponse);
        when(currencyService.currencyRateWithCodeExists(incomeRequest.getCurrency())).thenReturn(true);
        when(jwtSecurityService.getCurrentUserUid()).thenReturn(account.getUserUid());

        // when
        IncomeDto result = incomeFacade.addIncome(incomeRequest);

        // then
        Assertions.assertEquals(incomeResponse, result);
    }
}
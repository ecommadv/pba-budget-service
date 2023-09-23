package com.pba.budgetservice.unit;

import com.PBA.budgetservice.controller.request.ExpenseCreateRequest;
import com.PBA.budgetservice.facade.ExpenseFacadeImpl;
import com.PBA.budgetservice.mapper.ExpenseMapper;
import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.Expense;
import com.PBA.budgetservice.persistance.model.ExpenseCategory;
import com.PBA.budgetservice.persistance.model.dtos.ExpenseDto;
import com.PBA.budgetservice.security.JwtSecurityService;
import com.PBA.budgetservice.service.AccountService;
import com.PBA.budgetservice.service.CurrencyService;
import com.PBA.budgetservice.service.ExpenseCategoryService;
import com.PBA.budgetservice.service.ExpenseService;
import com.pba.budgetservice.mockgenerators.AccountMockGenerator;
import com.pba.budgetservice.mockgenerators.ExpenseCategoryMockGenerator;
import com.pba.budgetservice.mockgenerators.ExpenseMockGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExpenseFacadeUnitTest {
    @InjectMocks
    private ExpenseFacadeImpl expenseFacade;

    @Mock
    private ExpenseService expenseService;

    @Mock
    private ExpenseMapper expenseMapper;

    @Mock
    private AccountService accountService;

    @Mock
    private ExpenseCategoryService expenseCategoryService;

    @Mock
    private CurrencyService currencyService;

    @Mock
    private JwtSecurityService jwtSecurityService;

    @Test
    public void testAddExpenseRequest() {
        // given
        List<Account> accountList = AccountMockGenerator.generateMockListOfAccounts(3);
        List<ExpenseCategory> expenseCategories = ExpenseCategoryMockGenerator.generateMockListOfExpenseCategories(3);
        ExpenseCreateRequest expenseCreateRequest = ExpenseMockGenerator.generateMockExpenseCreateRequest(expenseCategories);
        Expense expense = ExpenseMockGenerator.generateMockExpense(expenseCategories, accountList);
        Account account = AccountMockGenerator.generateMockAccount();
        ExpenseCategory expenseCategory = ExpenseCategoryMockGenerator.generateMockExpenseCategory();
        ExpenseDto expenseDto = ExpenseDto.builder().build();
        UUID userUid = UUID.randomUUID();
        when(expenseMapper.toExpense(expenseCreateRequest)).thenReturn(expense);
        when(expenseMapper.toAccount(expenseCreateRequest, userUid)).thenReturn(account);
        when(accountService.addAccount(account)).thenReturn(account);
        when(expenseCategoryService.getByUid(expenseCreateRequest.getCategoryUid())).thenReturn(expenseCategory);
        when(expenseService.addExpense(expense)).thenReturn(expense);
        when(expenseMapper.toExpenseDto(expense, expenseCategory.getName())).thenReturn(expenseDto);
        when(currencyService.currencyRateWithCodeExists(expenseCreateRequest.getCurrency())).thenReturn(true);
        when(jwtSecurityService.getCurrentUserUid()).thenReturn(userUid);

        // when
        ExpenseDto result = expenseFacade.addExpense(expenseCreateRequest);

        // then
        Assertions.assertEquals(expenseDto, result);
    }
}

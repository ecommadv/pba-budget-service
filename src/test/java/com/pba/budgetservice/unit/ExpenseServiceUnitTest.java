package com.pba.budgetservice.unit;

import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.Expense;
import com.PBA.budgetservice.persistance.model.ExpenseCategory;
import com.PBA.budgetservice.persistance.repository.ExpenseDao;
import com.PBA.budgetservice.service.ExpenseServiceImpl;
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

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceUnitTest {
    @InjectMocks
    private ExpenseServiceImpl expenseService;

    @Mock
    private ExpenseDao expenseDao;

    @Test
    public void testAdd() {
        // given
        List<ExpenseCategory> expenseCategories = ExpenseCategoryMockGenerator.generateMockListOfExpenseCategories(3);
        List<Account> accounts = AccountMockGenerator.generateMockListOfAccounts(3);
        Expense expense = ExpenseMockGenerator.generateMockExpense(expenseCategories, accounts);
        when(expenseDao.save(expense)).thenReturn(expense);

        // when
        Expense result = expenseService.addExpense(expense);

        // then
        Assertions.assertEquals(expense, result);
    }
}

package com.PBA.budgetservice.facade;

import com.PBA.budgetservice.controller.request.ExpenseCreateRequest;
import com.PBA.budgetservice.mapper.ExpenseMapper;
import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.Expense;
import com.PBA.budgetservice.persistance.model.ExpenseCategory;
import com.PBA.budgetservice.persistance.model.dtos.ExpenseDto;
import com.PBA.budgetservice.service.AccountService;
import com.PBA.budgetservice.service.ExpenseCategoryService;
import com.PBA.budgetservice.service.ExpenseService;
import org.springframework.stereotype.Component;

@Component
public class ExpenseFacadeImpl implements ExpenseFacade {
    private final ExpenseService expenseService;
    private final ExpenseCategoryService expenseCategoryService;
    private final AccountService accountService;
    private final ExpenseMapper expenseMapper;

    public ExpenseFacadeImpl(ExpenseService expenseService, ExpenseCategoryService expenseCategoryService, AccountService accountService, ExpenseMapper expenseMapper) {
        this.expenseService = expenseService;
        this.expenseCategoryService = expenseCategoryService;
        this.accountService = accountService;
        this.expenseMapper = expenseMapper;
    }

    @Override
    public ExpenseDto addExpense(ExpenseCreateRequest expenseCreateRequest) {
        Account account = expenseMapper.toAccount(expenseCreateRequest);
        Expense expense = expenseMapper.toExpense(expenseCreateRequest);

        Account savedAccount = accountService.addAccount(account);
        expense.setAccountId(savedAccount.getId());

        ExpenseCategory expenseCategory = expenseCategoryService.getByUid(expenseCreateRequest.getCategoryUid());
        expense.setCategoryId(expenseCategory.getId());

        Expense savedExpense = expenseService.addExpense(expense);
        return expenseMapper.toExpenseDto(savedExpense, expenseCategory.getName());
    }
}

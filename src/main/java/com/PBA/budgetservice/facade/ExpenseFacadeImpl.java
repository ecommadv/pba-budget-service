package com.PBA.budgetservice.facade;

import com.PBA.budgetservice.controller.request.ExpenseCreateRequest;
import com.PBA.budgetservice.controller.request.ExpenseUpdateRequest;
import com.PBA.budgetservice.mapper.ExpenseMapper;
import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.Expense;
import com.PBA.budgetservice.persistance.model.ExpenseCategory;
import com.PBA.budgetservice.persistance.model.dtos.ExpenseDto;
import com.PBA.budgetservice.service.AccountService;
import com.PBA.budgetservice.service.ExpenseCategoryService;
import com.PBA.budgetservice.service.ExpenseService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    @Override
    public ExpenseDto updateExpense(ExpenseUpdateRequest expenseUpdateRequest, UUID uid) {
        Expense expenseToUpdate = expenseService.getByUid(uid);
        ExpenseCategory expenseCategory = expenseUpdateRequest.getCategoryUid() == null
                ? expenseCategoryService.getById(expenseToUpdate.getCategoryId())
                : expenseCategoryService.getByUid(expenseUpdateRequest.getCategoryUid());

        Expense updatedExpense = expenseMapper.toExpense(expenseUpdateRequest, expenseToUpdate, expenseCategory);
        Expense expenseResult = expenseService.updateExpense(updatedExpense);
        return expenseMapper.toExpenseDto(expenseResult, expenseCategory.getName());
    }

    @Override
    public List<ExpenseDto> getAllExpenses() {
        List<Expense> expenses = expenseService.getAll();
        Map<Long, String> categoryIdToNameMapping = expenseCategoryService.getIdToNameMapping();
        return expenseMapper.toExpenseDto(expenses, categoryIdToNameMapping);
    }

    @Override
    public void deleteExpenseByUid(UUID uid) {
        Expense expenseToDelete = expenseService.getByUid(uid);
        expenseService.deleteById(expenseToDelete.getId());
    }
}

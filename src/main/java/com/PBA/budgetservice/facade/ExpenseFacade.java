package com.PBA.budgetservice.facade;

import com.PBA.budgetservice.controller.request.ExpenseCreateRequest;
import com.PBA.budgetservice.controller.request.ExpenseUpdateRequest;
import com.PBA.budgetservice.persistance.model.dtos.ExpenseDto;

import java.util.List;
import java.util.UUID;

public interface ExpenseFacade {
    public ExpenseDto addExpense(ExpenseCreateRequest expenseCreateRequest);
    public ExpenseDto updateExpense(ExpenseUpdateRequest expenseUpdateRequest, UUID uid);
    public List<ExpenseDto> getAllExpenses();
    public void deleteExpenseByUid(UUID uid);
}

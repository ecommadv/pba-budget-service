package com.PBA.budgetservice.facade;

import com.PBA.budgetservice.controller.request.ExpenseCreateRequest;
import com.PBA.budgetservice.persistance.model.dtos.ExpenseDto;

public interface ExpenseFacade {
    public ExpenseDto addExpense(ExpenseCreateRequest expenseCreateRequest);
}

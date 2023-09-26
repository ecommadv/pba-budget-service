package com.PBA.budgetservice.facade;

import com.PBA.budgetservice.persistance.model.dtos.ExpenseCategoryDto;
import com.PBA.budgetservice.persistance.model.dtos.ExpenseDto;
import com.PBA.budgetservice.controller.request.ExpenseCreateRequest;
import com.PBA.budgetservice.controller.request.ExpenseUpdateRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ExpenseFacade {
    public ExpenseDto addExpense(ExpenseCreateRequest expenseCreateRequest);
    public ExpenseDto updateExpense(ExpenseUpdateRequest expenseUpdateRequest, UUID uid);
    public void deleteExpenseByUid(UUID uid);
    public List<ExpenseCategoryDto> getAllExpenseCategories();
    public List<ExpenseDto> getAllExpensesByUserAndCurrency(String currency);
    public List<ExpenseDto> getAllUserExpensesByName(String name);
    public List<ExpenseDto> getAllUserExpensesByCategoryName(String categoryName);
    public List<ExpenseDto> getAllExpensesByUserAndDate(LocalDateTime after, LocalDateTime before);
}

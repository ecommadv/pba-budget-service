package com.PBA.budgetservice.service;

import com.PBA.budgetservice.persistance.model.Expense;

import java.util.List;
import java.util.UUID;

public interface ExpenseService {
    public Expense addExpense(Expense expense);
    public Expense getByUid(UUID uid);
    public Expense updateExpense(Expense expense);
    public List<Expense> getAll();
    public Expense deleteById(Long id);
    public List<Expense> getByAccountId(Long accountId);
}

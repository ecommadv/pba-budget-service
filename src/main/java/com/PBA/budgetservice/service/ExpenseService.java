package com.PBA.budgetservice.service;

import com.PBA.budgetservice.persistance.model.Expense;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public interface ExpenseService {
    public Expense addExpense(Expense expense);
    public Expense getByUid(UUID uid);
    public Expense updateExpense(Expense expense);
    public List<Expense> getAll();
    public Expense deleteById(Long id);
    public List<Expense> getByAccountId(Long accountId);
    public List<Expense> getAllExpensesByUserUidAndFilter(UUID userUid, Function<Expense, Boolean> filterFct);
    public List<Expense> getAllExpensesByUserUidAndExpenseName(UUID userUid, String name);
    public List<Expense> getAllExpensesByUserUidAndCategoryName(UUID userUid, String categoryName);
    public List<Expense> getAllExpensesByUserUidAndDateBefore(UUID userUid, LocalDateTime before);
    public List<Expense> getAllExpensesByUserUidAndDateAfter(UUID userUid, LocalDateTime after);
    public List<Expense> getAllExpensesByUserUidAndDateBetween(UUID userUid, LocalDateTime after, LocalDateTime before);
}

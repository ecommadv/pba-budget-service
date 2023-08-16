package com.PBA.budgetservice.persistance.repository;

import com.PBA.budgetservice.persistance.model.ExpenseCategory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExpenseCategoryDao {
    public ExpenseCategory save(ExpenseCategory expenseCategory);
    public Optional<ExpenseCategory> getById(Long id);
    public List<ExpenseCategory> getAll();
    public ExpenseCategory deleteById(Long id);
    public ExpenseCategory update(ExpenseCategory expenseCategory, Long id);
    public Optional<ExpenseCategory> getByUid(UUID categoryUid);
}

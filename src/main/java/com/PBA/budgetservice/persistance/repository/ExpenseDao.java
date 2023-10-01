package com.PBA.budgetservice.persistance.repository;

import com.PBA.budgetservice.controller.request.DateRange;
import com.PBA.budgetservice.persistance.model.Expense;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExpenseDao {
    public Expense save(Expense expense);
    public Optional<Expense> getById(Long id);
    public List<Expense> getAll();
    public Expense deleteById(Long id);
    public Expense update(Expense expense, Long id);
    public Optional<Expense> getByUid(UUID uid);
    public List<Expense> getAllByUserUid(UUID userUid);
    public List<Expense> getAllByFilters(UUID userUid, String name, String categoryName, String currency, DateRange dateRange);
}

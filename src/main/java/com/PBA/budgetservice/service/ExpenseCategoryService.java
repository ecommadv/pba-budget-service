package com.PBA.budgetservice.service;

import com.PBA.budgetservice.persistance.model.ExpenseCategory;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ExpenseCategoryService {
    public ExpenseCategory getByUid(UUID categoryUid);
    public ExpenseCategory getById(Long id);
    public Map<Long, String> getIdToNameMapping();

    public List<ExpenseCategory> getAll();
}

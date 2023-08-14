package com.PBA.budgetservice.service;

import com.PBA.budgetservice.persistance.model.ExpenseCategory;

import java.util.UUID;

public interface ExpenseCategoryService {
    public ExpenseCategory getByUid(UUID categoryUid);
}

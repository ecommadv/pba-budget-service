package com.PBA.budgetservice.service;

import com.PBA.budgetservice.persistance.model.IncomeCategory;

import java.util.Optional;
import java.util.UUID;

public interface IncomeCategoryService {
    public IncomeCategory getIncomeCategoryByUid(UUID uid);
}

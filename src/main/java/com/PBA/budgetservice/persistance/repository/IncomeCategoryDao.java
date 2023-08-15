package com.PBA.budgetservice.persistance.repository;

import com.PBA.budgetservice.persistance.model.IncomeCategory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IncomeCategoryDao {
    public IncomeCategory save(IncomeCategory incomeCategory);
    public Optional<IncomeCategory> getById(Long id);
    public List<IncomeCategory> getAll();
    public IncomeCategory deleteById(Long id);
    public IncomeCategory update(IncomeCategory incomeCategory, Long id);
    public Optional<IncomeCategory> getIncomeCategoryByUid(UUID uid);
}

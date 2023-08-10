package com.PBA.budgetservice.persistance.repository;

import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.Income;

import java.util.List;
import java.util.Optional;

public interface IncomeDao {
    public Income save(Income income);
    public Optional<Income> getById(Long id);
    public List<Income> getAll();
    public Income deleteById(Long id);
    public Income update(Income income, Long id);
}

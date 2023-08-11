package com.PBA.budgetservice.service;

import com.PBA.budgetservice.persistance.model.Income;
import java.util.List;
import java.util.UUID;

public interface IncomeService {
    public Income addIncome(Income income);
    public List<Income> getAllIncomes();
    public Income getIncomeByUid(UUID uid);
    public Income updateIncome(Income income);
    public Income deleteIncomeById(Long id);
    public Income getIncomeById(Long id);
}

package com.PBA.budgetservice.service;

import com.PBA.budgetservice.persistance.model.Income;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public interface IncomeService {
    public Income addIncome(Income income);
    public List<Income> getAllIncomes();
    public Income getIncomeByUid(UUID uid);
    public Income updateIncome(Income income);
    public Income deleteIncomeById(Long id);
    public Income getIncomeById(Long id);
    public List<Income> getIncomeByAccountId(Long accountId);
    public List<Income> getAllIncomesByUserUidAndFilter(UUID userUid, Function<Income, Boolean> filterFct);
    public List<Income> getAllIncomesByUserUidAndCategoryName(UUID userUid, String categoryName);
    public List<Income> getAllIncomesByUserUidAndDateBefore(UUID userUid, LocalDateTime before);
    public List<Income> getAllIncomesByUserUidAndDateAfter(UUID userUid, LocalDateTime after);
    public List<Income> getAllIncomesByUserUidAndDateBetween(UUID userUid, LocalDateTime after, LocalDateTime before);
}

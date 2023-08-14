package com.PBA.budgetservice.service;

import com.PBA.budgetservice.exceptions.BudgetServiceException;
import com.PBA.budgetservice.persistance.model.ExpenseCategory;
import com.PBA.budgetservice.persistance.repository.ExpenseCategoryDao;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ExpenseCategoryServiceImpl implements ExpenseCategoryService {
    public final ExpenseCategoryDao expenseCategoryDao;

    public ExpenseCategoryServiceImpl(ExpenseCategoryDao expenseCategoryDao) {
        this.expenseCategoryDao = expenseCategoryDao;
    }

    @Override
    public ExpenseCategory getByUid(UUID categoryUid) {
        return expenseCategoryDao.getByUid(categoryUid)
                .orElseThrow(() -> new BudgetServiceException(String.format("Expense category with uid %s does not exist!", categoryUid.toString())));
    }

    @Override
    public ExpenseCategory getById(Long id) {
        return expenseCategoryDao.getById(id)
                .orElseThrow(() -> new BudgetServiceException(String.format("Expense category with id %d does not exist!", id)));
    }

    @Override
    public Map<Long, String> getIdToNameMapping() {
        return expenseCategoryDao.getAll()
                .stream()
                .collect(Collectors.toMap(ExpenseCategory::getId, ExpenseCategory::getName));
    }
}

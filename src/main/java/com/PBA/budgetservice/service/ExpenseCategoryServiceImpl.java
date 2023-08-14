package com.PBA.budgetservice.service;

import com.PBA.budgetservice.exceptions.BudgetServiceException;
import com.PBA.budgetservice.persistance.model.ExpenseCategory;
import com.PBA.budgetservice.persistance.repository.ExpenseCategoryDao;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
}

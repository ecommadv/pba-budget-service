package com.PBA.budgetservice.service;

import com.PBA.budgetservice.persistance.model.Expense;
import com.PBA.budgetservice.persistance.repository.ExpenseDao;
import org.springframework.stereotype.Service;

@Service
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseDao expenseDao;

    public ExpenseServiceImpl(ExpenseDao expenseDao) {
        this.expenseDao = expenseDao;
    }

    @Override
    public Expense addExpense(Expense expense) {
        return expenseDao.save(expense);
    }
}

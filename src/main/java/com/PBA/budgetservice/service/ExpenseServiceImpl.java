package com.PBA.budgetservice.service;

import com.PBA.budgetservice.exceptions.BudgetServiceException;
import com.PBA.budgetservice.persistance.model.Expense;
import com.PBA.budgetservice.persistance.repository.ExpenseDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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

    @Override
    public Expense getByUid(UUID uid) {
        return expenseDao.getByUid(uid)
                .orElseThrow(() -> new BudgetServiceException(String.format("Expense with uid %s does not exist!", uid.toString())));
    }

    @Override
    public Expense updateExpense(Expense expense) {
        return expenseDao.update(expense, expense.getId());
    }

    @Override
    public List<Expense> getAll() {
        return expenseDao.getAll();
    }
}

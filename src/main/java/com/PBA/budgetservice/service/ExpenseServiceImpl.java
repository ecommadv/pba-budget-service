package com.PBA.budgetservice.service;

import com.PBA.budgetservice.controller.request.DateRange;
import com.PBA.budgetservice.exceptions.EntityNotFoundException;
import com.PBA.budgetservice.exceptions.ErrorCodes;
import com.PBA.budgetservice.persistance.model.Expense;
import com.PBA.budgetservice.persistance.model.Repetition;
import com.PBA.budgetservice.persistance.repository.AccountDao;
import com.PBA.budgetservice.persistance.repository.ExpenseCategoryDao;
import com.PBA.budgetservice.persistance.repository.ExpenseDao;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseDao expenseDao;
    private final AccountDao accountDao;
    private final ExpenseCategoryDao expenseCategoryDao;

    public ExpenseServiceImpl(ExpenseDao expenseDao, AccountDao accountDao, ExpenseCategoryDao expenseCategoryDao) {
        this.expenseDao = expenseDao;
        this.accountDao = accountDao;
        this.expenseCategoryDao = expenseCategoryDao;
    }

    @Override
    public Expense addExpense(Expense expense) {
        return expenseDao.save(expense);
    }

    @Override
    public Expense getByUid(UUID uid) {
        return expenseDao.getByUid(uid)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Expense with uid %s does not exist!", uid.toString()),
                        ErrorCodes.EXPENSE_NOT_FOUND));
    }

    @Override
    public Expense updateExpense(Expense expense) {
        return expenseDao.update(expense, expense.getId());
    }

    @Override
    public List<Expense> getAll() {
        return expenseDao.getAll();
    }

    @Override
    public Expense deleteById(Long id) {
        return expenseDao.deleteById(id);
    }

    @Override
    public List<Expense> getAll(UUID userUid, String name, String categoryName, String currency, DateRange dateRange) {
        return expenseDao.getAllByFilters(userUid, name, categoryName, currency, dateRange);
    }

    @Override
    public List<Expense> getByRepetition(Repetition repetition) {
        return expenseDao.getByRepetition(repetition);
    }
}

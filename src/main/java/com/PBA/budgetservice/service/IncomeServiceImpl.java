package com.PBA.budgetservice.service;

import com.PBA.budgetservice.controller.request.DateRange;
import com.PBA.budgetservice.exceptions.EntityNotFoundException;
import com.PBA.budgetservice.exceptions.ErrorCodes;
import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.ExpenseCategory;
import com.PBA.budgetservice.persistance.model.Income;
import com.PBA.budgetservice.persistance.model.Repetition;
import com.PBA.budgetservice.persistance.repository.AccountDao;
import com.PBA.budgetservice.persistance.repository.ExpenseCategoryDao;
import com.PBA.budgetservice.persistance.repository.IncomeDao;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class IncomeServiceImpl implements IncomeService {
    private final IncomeDao incomeDao;
    private final AccountDao accountDao;
    private final ExpenseCategoryDao expenseCategoryDao;

    public IncomeServiceImpl(IncomeDao incomeDao, AccountDao accountDao, ExpenseCategoryDao expenseCategoryDao) {
        this.incomeDao = incomeDao;
        this.accountDao = accountDao;
        this.expenseCategoryDao = expenseCategoryDao;
    }

    @Override
    public Income addIncome(Income income) {
        return incomeDao.save(income);
    }

    @Override
    public List<Income> getAllIncomes() {
        return incomeDao.getAll();
    }

    @Override
    public Income getIncomeByUid(UUID uid) {
        return incomeDao.getByUid(uid)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Income with uid %s does not exist!", uid.toString()),
                        ErrorCodes.INCOME_NOT_FOUND));
    }

    @Override
    public Income updateIncome(Income income) {
        return incomeDao.update(income, income.getId());
    }

    @Override
    public Income deleteIncomeById(Long id) {
        return incomeDao.deleteById(id);
    }

    @Override
    public Income getIncomeById(Long id) {
        return incomeDao.getById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Income with id %d does not exist!", id),
                        ErrorCodes.INCOME_NOT_FOUND));
    }

    @Override
    public List<Income> getIncomeByAccountId(Long accountId) {
        return incomeDao.getAll()
                .stream()
                .filter(income -> Objects.equals(income.getAccountId(), accountId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Income> getAllByFilters(UUID userUid, String categoryName, String currency, DateRange dateRange) {
        return incomeDao.getAllByFilters(userUid, categoryName, currency, dateRange);
    }

    @Override
    public List<Income> getByRepetition(Repetition repetition) {
        return incomeDao.getByRepetition(repetition);
    }
}

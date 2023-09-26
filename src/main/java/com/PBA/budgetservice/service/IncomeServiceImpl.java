package com.PBA.budgetservice.service;

import com.PBA.budgetservice.exceptions.EntityNotFoundException;
import com.PBA.budgetservice.exceptions.ErrorCodes;
import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.ExpenseCategory;
import com.PBA.budgetservice.persistance.model.Income;
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
    public List<Income> getAllIncomesByUserUidAndFilter(UUID userUid, Function<Income, Boolean> filterFct) {
        Set<Long> accountIds = this.getAccountIdsByUserUid(userUid);
        return incomeDao
                .getAll()
                .stream()
                .filter(income -> accountIds.contains(income.getAccountId()) && filterFct.apply(income))
                .toList();
    }

    @Override
    public List<Income> getAllIncomesByUserUidAndCategoryName(UUID userUid, String categoryName) {
        Map<Long, String> incomeCategoryIdToName = this.getCategoryIdToNameMapping();
        return this.getAllIncomesByUserUidAndFilter(userUid, income -> incomeCategoryIdToName.get(income.getCategoryId()).equals(categoryName));
    }

    @Override
    public List<Income> getAllIncomesByUserUidAndDateBefore(UUID userUid, LocalDateTime before) {
        return this.getAllIncomesByUserUidAndFilter(userUid, income -> income.getCreatedAt().isBefore(before));
    }

    @Override
    public List<Income> getAllIncomesByUserUidAndDateAfter(UUID userUid, LocalDateTime after) {
        return this.getAllIncomesByUserUidAndFilter(userUid, income -> income.getCreatedAt().isAfter(after));
    }

    @Override
    public List<Income> getAllIncomesByUserUidAndDateBetween(UUID userUid, LocalDateTime after, LocalDateTime before) {
        return this.getAllIncomesByUserUidAndFilter(userUid, income -> income.getCreatedAt().isBefore(before)
                && income.getCreatedAt().isAfter(after));
    }

    private Set<Long> getAccountIdsByUserUid(UUID userUid) {
        return accountDao
                .getAll()
                .stream()
                .filter(account -> account.getUserUid().equals(userUid))
                .map(Account::getId)
                .collect(Collectors.toSet());
    }

    private Map<Long, String> getCategoryIdToNameMapping() {
        return expenseCategoryDao
                .getAll()
                .stream()
                .collect(Collectors.toMap(ExpenseCategory::getId, ExpenseCategory::getName));
    }
}

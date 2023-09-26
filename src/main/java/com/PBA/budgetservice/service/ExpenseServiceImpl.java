package com.PBA.budgetservice.service;

import com.PBA.budgetservice.exceptions.EntityNotFoundException;
import com.PBA.budgetservice.exceptions.ErrorCodes;
import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.Expense;
import com.PBA.budgetservice.persistance.model.ExpenseCategory;
import com.PBA.budgetservice.persistance.repository.AccountDao;
import com.PBA.budgetservice.persistance.repository.ExpenseCategoryDao;
import com.PBA.budgetservice.persistance.repository.ExpenseDao;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public List<Expense> getByAccountId(Long accountId) {
        return expenseDao.getAll()
                .stream()
                .filter(expense -> Objects.equals(expense.getAccountId(), accountId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Expense> getAllExpensesByUserUidAndFilter(UUID userUid, Function<Expense, Boolean> filterFct) {
        Set<Long> accountIds = this.getAccountIdsByUserUid(userUid);
        return expenseDao
                .getAll()
                .stream()
                .filter(expense -> accountIds.contains(expense.getAccountId()) && filterFct.apply(expense))
                .toList();
    }

    @Override
    public List<Expense> getAllExpensesByUserUidAndExpenseName(UUID userUid, String name) {
        return this.getAllExpensesByUserUidAndFilter(userUid, expense -> expense.getName().equals(name));
    }

    @Override
    public List<Expense> getAllExpensesByUserUidAndCategoryName(UUID userUid, String categoryName) {
        Map<Long, String> categoryIdToNameMapping = this.getCategoryIdToNameMapping();
        return this.getAllExpensesByUserUidAndFilter(userUid, expense -> categoryIdToNameMapping.get(expense.getCategoryId()).equals(categoryName));
    }

    @Override
    public List<Expense> getAllExpensesByUserUidAndDateBefore(UUID userUid, LocalDateTime before) {
        return this.getAllExpensesByUserUidAndFilter(userUid, expense -> expense.getCreatedAt().isBefore(before));
    }

    @Override
    public List<Expense> getAllExpensesByUserUidAndDateAfter(UUID userUid, LocalDateTime after) {
        return this.getAllExpensesByUserUidAndFilter(userUid, expense -> expense.getCreatedAt().isAfter(after));
    }

    @Override
    public List<Expense> getAllExpensesByUserUidAndDateBetween(UUID userUid, LocalDateTime after, LocalDateTime before) {
        return this.getAllExpensesByUserUidAndFilter(userUid, expense -> expense.getCreatedAt().isAfter(after)
                && expense.getCreatedAt().isBefore(before));
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

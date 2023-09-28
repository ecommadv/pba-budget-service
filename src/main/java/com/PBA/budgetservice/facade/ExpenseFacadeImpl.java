package com.PBA.budgetservice.facade;

import com.PBA.budgetservice.exceptions.AuthorizationException;
import com.PBA.budgetservice.exceptions.EntityNotFoundException;
import com.PBA.budgetservice.exceptions.ErrorCodes;
import com.PBA.budgetservice.gateway.UserGateway;
import com.PBA.budgetservice.persistance.model.Expense;
import com.PBA.budgetservice.persistance.model.ExpenseCategory;
import com.PBA.budgetservice.persistance.model.dtos.ExpenseDto;
import com.PBA.budgetservice.security.JwtSecurityService;
import com.PBA.budgetservice.security.Permission;
import com.PBA.budgetservice.security.TokenType;
import com.PBA.budgetservice.service.AccountService;
import com.PBA.budgetservice.service.CurrencyService;
import com.PBA.budgetservice.service.ExpenseCategoryService;
import com.PBA.budgetservice.service.ExpenseService;
import com.PBA.budgetservice.controller.request.ExpenseCreateRequest;
import com.PBA.budgetservice.controller.request.ExpenseUpdateRequest;
import com.PBA.budgetservice.mapper.ExpenseMapper;
import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.dtos.ExpenseCategoryDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class ExpenseFacadeImpl implements ExpenseFacade {
    private final ExpenseService expenseService;
    private final ExpenseCategoryService expenseCategoryService;
    private final AccountService accountService;
    private final ExpenseMapper expenseMapper;
    private final CurrencyService currencyService;
    private final JwtSecurityService jwtSecurityService;

    public ExpenseFacadeImpl(ExpenseService expenseService, ExpenseCategoryService expenseCategoryService, AccountService accountService, ExpenseMapper expenseMapper, UserGateway userGateway, CurrencyService currencyService, JwtSecurityService jwtSecurityService) {
        this.expenseService = expenseService;
        this.expenseCategoryService = expenseCategoryService;
        this.accountService = accountService;
        this.expenseMapper = expenseMapper;
        this.jwtSecurityService = jwtSecurityService;
        this.currencyService = currencyService;
    }

    @Override
    public ExpenseDto addExpense(ExpenseCreateRequest expenseCreateRequest) {
        jwtSecurityService.validateHasPermission(Permission.CREATE_EXPENSE);
        this.validateCurrencyCodeExists(expenseCreateRequest.getCurrency());
        UUID ownerUid = jwtSecurityService.getCurrentAccountOwnerUid();
        Account account = expenseMapper.toAccount(expenseCreateRequest, ownerUid);
        Expense expense = expenseMapper.toExpense(expenseCreateRequest);

        Account savedAccount = accountService.addAccount(account);
        expense.setAccountId(savedAccount.getId());

        ExpenseCategory expenseCategory = expenseCategoryService.getByUid(expenseCreateRequest.getCategoryUid());
        expense.setCategoryId(expenseCategory.getId());

        Expense savedExpense = expenseService.addExpense(expense);
        return expenseMapper.toExpenseDto(savedExpense, expenseCategory.getName());
    }

    @Override
    public ExpenseDto updateExpense(ExpenseUpdateRequest expenseUpdateRequest, UUID uid) {
        jwtSecurityService.validateHasPermission(Permission.UPDATE_EXPENSE);
        UUID ownerUid = jwtSecurityService.getCurrentAccountOwnerUid();
        Expense expenseToUpdate = expenseService.getByUid(uid);
        this.validateExpenseIsOwnedByUser(expenseToUpdate, ownerUid);
        ExpenseCategory expenseCategory = this.getUpdatedExpenseCategory(expenseUpdateRequest, expenseToUpdate);

        Expense updatedExpense = expenseMapper.toExpense(expenseUpdateRequest, expenseToUpdate, expenseCategory);
        Expense expenseResult = expenseService.updateExpense(updatedExpense);
        return expenseMapper.toExpenseDto(expenseResult, expenseCategory.getName());
    }

    @Override
    public void deleteExpenseByUid(UUID uid) {
        jwtSecurityService.validateHasPermission(Permission.DELETE_EXPENSE);
        UUID ownerUid = jwtSecurityService.getCurrentAccountOwnerUid();
        Expense expenseToDelete = expenseService.getByUid(uid);
        this.validateExpenseIsOwnedByUser(expenseToDelete, ownerUid);
        expenseService.deleteById(expenseToDelete.getId());
    }

    @Override
    public List<ExpenseCategoryDto> getAllExpenseCategories() {
        List<ExpenseCategory> expenseCategories = expenseCategoryService.getAll();
        return expenseMapper.toExpenseCategoryDto(expenseCategories);
    }

    @Override
    public List<ExpenseDto> getAllExpensesByUserAndCurrency(String currency) {
        jwtSecurityService.validateHasPermission(Permission.GET_EXPENSES);
        UUID ownerUid = jwtSecurityService.getCurrentAccountOwnerUid();
        Account account = accountService.getByUserUidAndCurrency(ownerUid, currency);
        List<Expense> expenses = expenseService.getByAccountId(account.getId());

        Map<Long, String> categoryIdToNameMapping = expenseCategoryService.getIdToNameMapping();
        return expenseMapper.toExpenseDto(expenses, categoryIdToNameMapping);
    }

    @Override
    public List<ExpenseDto> getAllUserExpensesByName(String name) {
        jwtSecurityService.validateHasPermission(Permission.GET_EXPENSES);
        UUID ownerUid = jwtSecurityService.getCurrentAccountOwnerUid();
        List<Expense> expenses = expenseService.getAllExpensesByUserUidAndExpenseName(ownerUid, name);

        Map<Long, String> categoryIdToNameMapping = expenseCategoryService.getIdToNameMapping();
        return expenseMapper.toExpenseDto(expenses, categoryIdToNameMapping);
    }

    @Override
    public List<ExpenseDto> getAllUserExpensesByCategoryName(String categoryName) {
        jwtSecurityService.validateHasPermission(Permission.GET_EXPENSES);
        UUID ownerUid = jwtSecurityService.getCurrentAccountOwnerUid();
        List<Expense> expenses = expenseService.getAllExpensesByUserUidAndCategoryName(ownerUid, categoryName);

        Map<Long, String> categoryIdToNameMapping = expenseCategoryService.getIdToNameMapping();
        return expenseMapper.toExpenseDto(expenses, categoryIdToNameMapping);
    }

    @Override
    public List<ExpenseDto> getAllExpensesByUserAndDate(LocalDateTime after, LocalDateTime before) {
        jwtSecurityService.validateHasPermission(Permission.GET_EXPENSES);
        if (after == null && before == null) {
            return List.of();
        }
        UUID ownerUid = jwtSecurityService.getCurrentAccountOwnerUid();
        List<Expense> expenses =
                after == null
                ? expenseService.getAllExpensesByUserUidAndDateBefore(ownerUid, before)
                : before == null
                    ? expenseService.getAllExpensesByUserUidAndDateAfter(ownerUid, after)
                : expenseService.getAllExpensesByUserUidAndDateBetween(ownerUid, after, before);

        Map<Long, String> categoryIdToNameMapping = expenseCategoryService.getIdToNameMapping();
        return expenseMapper.toExpenseDto(expenses, categoryIdToNameMapping);
    }

    private ExpenseCategory getUpdatedExpenseCategory(ExpenseUpdateRequest expenseUpdateRequest, Expense expenseToUpdate) {
        return expenseUpdateRequest.getCategoryUid() == null
                ? expenseCategoryService.getById(expenseToUpdate.getCategoryId())
                : expenseCategoryService.getByUid(expenseUpdateRequest.getCategoryUid());
    }

    private void validateExpenseIsOwnedByUser(Expense expense, UUID userUid) {
        Account account = accountService.getById(expense.getAccountId());
        if (!account.getUserUid().equals(userUid)) {
            throw new AuthorizationException();
        }
    }

    private void validateCurrencyCodeExists(String code) {
        if (!currencyService.currencyRateWithCodeExists(code)) {
            throw new EntityNotFoundException(
                    String.format("Currency rate with code %s does not exist", code),
                    ErrorCodes.CURRENCY_RATE_NOT_FOUND
            );
        }
    }
}

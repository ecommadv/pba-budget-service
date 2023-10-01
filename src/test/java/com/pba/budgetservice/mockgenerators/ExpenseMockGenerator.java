package com.pba.budgetservice.mockgenerators;

import com.PBA.budgetservice.controller.request.ExpenseCreateRequest;
import com.PBA.budgetservice.controller.request.ExpenseUpdateRequest;
import com.PBA.budgetservice.persistance.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExpenseMockGenerator {
    public static Expense generateMockExpense(List<ExpenseCategory> expenseCategories,
                                              List<Account> accounts,
                                              List<CurrencyRate> currencyRates) {
        return generateMockExpense(
                getRandomCategory(expenseCategories).getId(),
                getRandomAccount(accounts).getId(),
                getRandomCurrency(currencyRates)
        );
    }

    public static Expense generateMockExpense(List<ExpenseCategory> expenseCategories,
                                              List<Account> accounts) {
        return generateMockExpense(
                getRandomCategory(expenseCategories).getId(),
                getRandomAccount(accounts).getId(),
                UUID.randomUUID().toString()
        );
    }

    public static Expense generateMockExpense(long categoryId, long accountId, String currency) {
        return Expense.builder()
                .id(new Random().nextLong())
                .amount(BigDecimal.valueOf(new Random().nextDouble()))
                .name(UUID.randomUUID().toString())
                .description(UUID.randomUUID().toString())
                .currency(currency)
                .uid(UUID.randomUUID())
                .accountId(accountId)
                .categoryId(categoryId)
                .createdAt(LocalDateTime.of(LocalDate.now().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth(), 0, 0, 0))
                .repetition(getRandomRepetition())
                .build();
    }

    public static List<Expense> generateMockListOfExpenses(List<ExpenseCategory> expenseCategories,
                                                           List<Account> accounts,
                                                           List<CurrencyRate> currencyRates,
                                                           int size) {
        return Stream.generate(() -> ExpenseMockGenerator.generateMockExpense(expenseCategories, accounts, currencyRates))
                .limit(size)
                .collect(Collectors.toList());
    }

    public static List<Expense> generateMockListOfExpenses(List<ExpenseCategory> expenseCategories,
                                                           List<Account> accounts,
                                                           int size) {
        return Stream.generate(() -> ExpenseMockGenerator.generateMockExpense(expenseCategories, accounts))
                .limit(size)
                .collect(Collectors.toList());
    }

    public static ExpenseCreateRequest generateMockExpenseCreateRequest(List<ExpenseCategory> expenseCategories,
                                                                        List<CurrencyRate> currencyRates) {
        return generateMockExpenseCreateRequest(getRandomCategory(expenseCategories).getUid(), getRandomCurrency(currencyRates));
    }

    public static ExpenseCreateRequest generateMockExpenseCreateRequest(List<ExpenseCategory> expenseCategories) {
        return generateMockExpenseCreateRequest(getRandomCategory(expenseCategories).getUid(), UUID.randomUUID().toString());
    }

    public static ExpenseCreateRequest generateMockExpenseCreateRequest(UUID categoryUid,
                                                                        String currency) {
        return ExpenseCreateRequest.builder()
                .amount(BigDecimal.valueOf(new Random().nextDouble()))
                .name(UUID.randomUUID().toString())
                .description(UUID.randomUUID().toString())
                .currency(currency)
                .categoryUid(categoryUid)
                .createdAt(LocalDateTime.of(LocalDate.now().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth(), 0, 0, 0))
                .build();
    }

    public static ExpenseUpdateRequest generateMockExpenseUpdateRequest(List<ExpenseCategory> expenseCategories) {
        if (expenseCategories.isEmpty()) {
            return null;
        }

        List<UUID> expenseCategoryUids = expenseCategories.stream().map(ExpenseCategory::getUid).collect(Collectors.toList());
        Collections.shuffle(expenseCategoryUids);

        return ExpenseUpdateRequest.builder()
                .amount(BigDecimal.valueOf(new Random().nextDouble()))
                .name(UUID.randomUUID().toString())
                .description(UUID.randomUUID().toString())
                .categoryUid(expenseCategoryUids.stream().findFirst().get())
                .createdAt(LocalDateTime.of(LocalDate.now().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth(), 0, 0, 0))
                .build();
    }

    private static String getRandomCurrency(List<CurrencyRate> currencyRates) {
        if (currencyRates.isEmpty()) {
            return "";
        }
        Collections.shuffle(currencyRates);
        return currencyRates.get(0).getCode();
    }

    private static ExpenseCategory getRandomCategory(List<ExpenseCategory> expenseCategories) {
        if (expenseCategories.isEmpty()) {
            return new ExpenseCategory();
        }
        Collections.shuffle(expenseCategories);
        return expenseCategories.get(0);
    }

    private static Account getRandomAccount(List<Account> accounts) {
        if (accounts.isEmpty()) {
            return new Account();
        }
        Collections.shuffle(accounts);
        return accounts.get(0);
    }

    private static Repetition getRandomRepetition() {
        List<Repetition> repetitions = new ArrayList<>(Arrays.stream(Repetition.values()).toList());
        Collections.shuffle(repetitions);
        return repetitions.get(0);
    }
}

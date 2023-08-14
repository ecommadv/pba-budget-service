package mockgenerators;

import com.PBA.budgetservice.controller.request.ExpenseCreateRequest;
import com.PBA.budgetservice.controller.request.ExpenseUpdateRequest;
import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.Expense;
import com.PBA.budgetservice.persistance.model.ExpenseCategory;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExpenseMockGenerator {
    public static Expense generateMockExpense(List<ExpenseCategory> expenseCategories, List<Account> accounts) {
        if (expenseCategories.isEmpty() || accounts.isEmpty()) {
            return null;
        }
        List<Long> expenseCategoryIdList = expenseCategories.stream().map(ExpenseCategory::getId).collect(Collectors.toList());
        Collections.shuffle(expenseCategoryIdList);
        List<Long> accountIdList = accounts.stream().map(Account::getId).collect(Collectors.toList());
        Collections.shuffle(accountIdList);
        return Expense.builder()
                .id(new Random().nextLong())
                .amount(BigDecimal.valueOf(new Random().nextDouble()))
                .name(UUID.randomUUID().toString())
                .description(UUID.randomUUID().toString())
                .currency(getRandomCurrency())
                .uid(UUID.randomUUID())
                .accountId(accountIdList.stream().findFirst().get())
                .categoryId(expenseCategoryIdList.stream().findFirst().get())
                .build();
    }

    public static List<Expense> generateMockListOfExpenses(List<ExpenseCategory> expenseCategories, List<Account> accounts, int size) {
        if (expenseCategories.isEmpty() || accounts.isEmpty()) {
            return null;
        }
        return Stream.generate(() -> ExpenseMockGenerator.generateMockExpense(expenseCategories, accounts))
                .limit(size)
                .collect(Collectors.toList());
    }

    public static ExpenseCreateRequest generateMockExpenseCreateRequest(List<ExpenseCategory> expenseCategories, List<Account> accounts) {
        if (expenseCategories.isEmpty() || accounts.isEmpty()) {
            return null;
        }

        List<UUID> expenseCategoryUids = expenseCategories.stream().map(ExpenseCategory::getUid).collect(Collectors.toList());
        Collections.shuffle(expenseCategoryUids);

        List<UUID> accountUids = accounts.stream().map(Account::getUserUid).collect(Collectors.toList());
        Collections.shuffle(accountUids);

        return ExpenseCreateRequest.builder()
                .amount(BigDecimal.valueOf(new Random().nextDouble()))
                .name(UUID.randomUUID().toString())
                .description(UUID.randomUUID().toString())
                .currency(getRandomCurrency())
                .userUid(accountUids.stream().findFirst().get())
                .categoryUid(expenseCategoryUids.stream().findFirst().get())
                .build();
    }

    private static String getRandomCurrency() {
        List<Currency> currencies = new ArrayList<>(Currency.getAvailableCurrencies());
        Collections.shuffle(currencies);
        return currencies.stream().findFirst().get().toString();
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
                .build();
    }
}

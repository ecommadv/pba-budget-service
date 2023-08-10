package mockgenerators;

import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.Income;
import com.PBA.budgetservice.persistance.model.IncomeCategory;
import com.PBA.budgetservice.persistance.model.dtos.IncomeRequest;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IncomeMockGenerator {
    public static Income generateMockIncome(List<IncomeCategory> incomeCategories, List<Account> accounts) {
        if (incomeCategories.isEmpty() || accounts.isEmpty()) {
            return null;
        }
        List<Long> incomeCategoryIdList = incomeCategories.stream().map(IncomeCategory::getId).collect(Collectors.toList());
        Collections.shuffle(incomeCategoryIdList);
        List<Long> accountIdList = accounts.stream().map(Account::getId).collect(Collectors.toList());
        Collections.shuffle(accountIdList);
        return Income.builder()
                .id(new Random().nextLong())
                .amount(BigDecimal.valueOf(new Random().nextDouble()))
                .description(UUID.randomUUID().toString())
                .currency(getRandomCurrency())
                .uid(UUID.randomUUID())
                .accountId(incomeCategoryIdList.stream().findFirst().get())
                .categoryId(accountIdList.stream().findFirst().get())
                .build();
    }

    public static List<Income> generateMockListOfIncomes(List<IncomeCategory> incomeCategories, List<Account> accounts, int size) {
        if (incomeCategories.isEmpty() || accounts.isEmpty()) {
            return null;
        }
        return Stream.generate(() -> IncomeMockGenerator.generateMockIncome(incomeCategories, accounts))
                .limit(size)
                .collect(Collectors.toList());
    }

    public static IncomeRequest generateMockIncomeRequest(List<IncomeCategory> incomeCategories, List<Account> accounts) {
        if (incomeCategories.isEmpty() || accounts.isEmpty()) {
            return null;
        }
        List<UUID> incomeCategoryUidList = incomeCategories.stream().map(IncomeCategory::getUid).collect(Collectors.toList());
        Collections.shuffle(incomeCategoryUidList);
        List<UUID> accountUidList = accounts.stream().map(Account::getUserUid).collect(Collectors.toList());
        Collections.shuffle(accountUidList);
        return IncomeRequest.builder()
                .amount(BigDecimal.valueOf(new Random().nextDouble()))
                .description(UUID.randomUUID().toString())
                .currency(getRandomCurrency())
                .userUid(accountUidList.stream().findFirst().get())
                .categoryUid(incomeCategoryUidList.stream().findFirst().get())
                .build();
    }

    private static String getRandomCurrency() {
        List<Currency> currencies = new ArrayList<>(Currency.getAvailableCurrencies());
        Collections.shuffle(currencies);
        return currencies.stream().findFirst().get().toString();
    }
}

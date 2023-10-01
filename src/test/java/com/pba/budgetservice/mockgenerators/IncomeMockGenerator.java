package com.pba.budgetservice.mockgenerators;

import com.PBA.budgetservice.controller.request.IncomeCreateRequest;
import com.PBA.budgetservice.controller.request.IncomeUpdateRequest;
import com.PBA.budgetservice.persistance.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IncomeMockGenerator {
    public static Income generateMockIncome(List<IncomeCategory> incomeCategories,
                                            List<Account> accounts,
                                            List<CurrencyRate> currencyRates) {
        return generateMockIncome(
                getRandomCategory(incomeCategories).getId(),
                getRandomAccount(accounts).getId(),
                getRandomCurrency(currencyRates)
        );
    }

    public static Income generateMockIncome(List<IncomeCategory> incomeCategories,
                                            List<Account> accounts) {
        return generateMockIncome(
                getRandomCategory(incomeCategories).getId(),
                getRandomAccount(accounts).getId(),
                UUID.randomUUID().toString()
        );
    }

    public static Income generateMockIncome(long categoryId, long accountId, String currency) {
        return Income.builder()
                .id(new Random().nextLong())
                .amount(BigDecimal.valueOf(new Random().nextDouble()))
                .description(UUID.randomUUID().toString())
                .currency(currency)
                .uid(UUID.randomUUID())
                .accountId(accountId)
                .categoryId(categoryId)
                .createdAt(LocalDateTime.of(LocalDate.now().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth(), 0, 0, 0))
                .repetition(getRandomRepetition())
                .build();
    }

    public static List<Income> generateMockListOfIncomes(List<IncomeCategory> incomeCategories,
                                                         List<Account> accounts,
                                                         List<CurrencyRate> currencyRates,
                                                         int size) {
        return Stream.generate(() -> IncomeMockGenerator.generateMockIncome(incomeCategories, accounts, currencyRates))
                .limit(size)
                .collect(Collectors.toList());
    }

    public static List<Income> generateMockListOfIncomes(List<IncomeCategory> incomeCategories,
                                                         List<Account> accounts,
                                                         int size) {
        return Stream.generate(() -> IncomeMockGenerator.generateMockIncome(incomeCategories, accounts))
                .limit(size)
                .collect(Collectors.toList());
    }

    public static IncomeCreateRequest generateMockIncomeCreateRequest(List<IncomeCategory> incomeCategories,
                                                                      List<CurrencyRate> currencyRates) {
        return generateMockIncomeCreateRequest(getRandomCategory(incomeCategories).getUid(), getRandomCurrency(currencyRates));
    }

    public static IncomeCreateRequest generateMockIncomeCreateRequest(List<IncomeCategory> incomeCategories) {
        return generateMockIncomeCreateRequest(getRandomCategory(incomeCategories).getUid(), UUID.randomUUID().toString());
    }

    public static IncomeCreateRequest generateMockIncomeCreateRequest(UUID categoryUid,
                                                                      String currency) {
        return IncomeCreateRequest.builder()
                .amount(BigDecimal.valueOf(new Random().nextDouble()))
                .description(UUID.randomUUID().toString())
                .currency(currency)
                .categoryUid(categoryUid)
                .createdAt(LocalDateTime.of(LocalDate.now().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth(), 0, 0, 0))
                .repetition(getRandomRepetition())
                .build();
    }

    public static IncomeUpdateRequest generateMockIncomeUpdateRequest(List<IncomeCategory> incomeCategories) {
        return IncomeUpdateRequest.builder()
                .amount(BigDecimal.valueOf(new Random().nextDouble()))
                .description(UUID.randomUUID().toString())
                .categoryUid(getRandomCategory(incomeCategories).getUid())
                .createdAt(LocalDateTime.of(LocalDate.now().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth(), 0, 0, 0))
                .repetition(getRandomRepetition())
                .build();
    }

    private static String getRandomCurrency(List<CurrencyRate> currencyRates) {
        if (currencyRates.isEmpty()) {
            return "";
        }
        Collections.shuffle(currencyRates);
        return currencyRates.get(0).getCode();
    }

    private static IncomeCategory getRandomCategory(List<IncomeCategory> incomeCategories) {
        if (incomeCategories.isEmpty()) {
            return new IncomeCategory();
        }
        Collections.shuffle(incomeCategories);
        return incomeCategories.get(0);
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

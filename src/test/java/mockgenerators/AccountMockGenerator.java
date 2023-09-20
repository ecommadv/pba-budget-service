package mockgenerators;

import com.PBA.budgetservice.controller.request.AccountCreateRequest;
import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.CurrencyRate;
import com.PBA.budgetservice.persistance.model.dtos.AccountDto;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AccountMockGenerator {
    public static Account generateMockAccount() {
        return Account.builder()
                .id(new Random().nextLong())
                .userUid(UUID.randomUUID())
                .currency(UUID.randomUUID().toString())
                .build();
    }

    public static List<Account> generateMockListOfAccounts(int size) {
        return Stream.generate(AccountMockGenerator::generateMockAccount)
                .limit(size)
                .collect(Collectors.toList());
    }

    public static AccountCreateRequest generateMockAccountCreateRequest(List<CurrencyRate> currencyRates) {
        return AccountCreateRequest.builder()
                .currency(getRandomCurrency(currencyRates))
                .build();
    }

    public static AccountCreateRequest generateMockAccountCreateRequest() {
        return AccountCreateRequest.builder()
                .currency(UUID.randomUUID().toString())
                .build();
    }

    public static AccountDto generateMockAccountDto(List<CurrencyRate> currencyRates) {
        return AccountDto.builder()
                .userUid(UUID.randomUUID())
                .currency(getRandomCurrency(currencyRates))
                .build();
    }

    public static AccountDto generateMockAccountDto() {
        return AccountDto.builder()
                .userUid(UUID.randomUUID())
                .currency(UUID.randomUUID().toString())
                .build();
    }

    public static Account generateMockAccount(String currency, UUID userUid) {
        return Account.builder()
                .id(new Random().nextLong())
                .userUid(userUid)
                .currency(currency)
                .build();
    }

    private static String getRandomCurrency(List<CurrencyRate> currencyRates) {
        if (currencyRates.isEmpty()) {
            return "";
        }
        Collections.shuffle(currencyRates);
        return currencyRates.get(0).getCode();
    }
}

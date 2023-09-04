package mockgenerators;

import com.PBA.budgetservice.controller.request.AccountCreateRequest;
import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.dtos.AccountDto;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AccountMockGenerator {
    public static Account generateMockAccount() {
        return Account.builder()
                .id(new Random().nextLong())
                .userUid(UUID.randomUUID())
                .currency(getRandomCurrency())
                .build();
    }

    public static List<Account> generateMockListOfAccounts(int size) {
        return Stream.generate(AccountMockGenerator::generateMockAccount)
                .limit(size)
                .collect(Collectors.toList());
    }

    public static AccountCreateRequest generateMockAccountCreateRequest() {
        return AccountCreateRequest.builder()
                .userUid(UUID.randomUUID())
                .currency(getRandomCurrency())
                .build();
    }

    public static AccountDto generateMockAccountDto() {
        return AccountDto.builder()
                .userUid(UUID.randomUUID())
                .currency(getRandomCurrency())
                .build();
    }

    private static String getRandomCurrency() {
        List<Currency> currencies = new ArrayList<>(Currency.getAvailableCurrencies());
        Collections.shuffle(currencies);
        return currencies.stream().findFirst().get().toString();
    }
}

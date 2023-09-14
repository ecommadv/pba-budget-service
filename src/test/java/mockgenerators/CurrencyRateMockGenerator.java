package mockgenerators;

import com.PBA.budgetservice.persistance.model.CurrencyRate;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CurrencyRateMockGenerator {
    public static CurrencyRate generateMockCurrencyRate() {
        return CurrencyRate.builder()
                .code(getRandomCode())
                .mainValue(BigDecimal.valueOf(new Random().nextDouble()))
                .build();
    }

    public static List<CurrencyRate> generateMockListOfCurrencyRates(int size) {
        return Stream.generate(CurrencyRateMockGenerator::generateMockCurrencyRate)
                .limit(size)
                .collect(Collectors.toList());
    }

    private static String getRandomCode() {
        List<Currency> currencies = new ArrayList<>(Currency.getAvailableCurrencies());
        Collections.shuffle(currencies);
        return currencies.stream().findFirst().get().toString();
    }
}

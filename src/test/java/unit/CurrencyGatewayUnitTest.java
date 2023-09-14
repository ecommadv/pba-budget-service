package unit;

import com.PBA.budgetservice.gateway.CurrencyGatewayImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.mockito.Answers;

import java.io.*;
import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CurrencyGatewayUnitTest {
    @InjectMocks
    private CurrencyGatewayImpl currencyGateway;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private WebClient webClient;

    @Value("${currency.rates.url}")
    private String bnrUri;

    @Test
    public void testGetExchangeRatesMapping() throws IOException {
        // given
        String xmlContent = this.getFileContentAsString("mock_exchange_rates.xml");
        Mono<String> responseMono = Mono.just(xmlContent);
        when(webClient.get().uri(bnrUri).retrieve().bodyToMono(String.class)).thenReturn(responseMono);
        Map<String, BigDecimal> expectedResult = this.getExpectedMapping();

        // when
        Map<String, BigDecimal> result = currencyGateway.getCurrencyExchangeRatesMapping();

        // then
        assertEquals(expectedResult, result);
    }

    private String getFileContentAsString(String fileName) throws IOException {
        ClassPathResource fileResource = new ClassPathResource(fileName);
        File file = fileResource.getFile();

        Reader fileReader = new FileReader(file);
        BufferedReader bufReader = new BufferedReader(fileReader);
        StringBuilder sb = new StringBuilder();
        String line = bufReader.readLine();
        while (line != null) {
            sb.append(line).append("\n");
            line = bufReader.readLine();
        }
        return sb.toString();
    }

    private Map<String, BigDecimal> getExpectedMapping() {
        return Map.of(
                "AED", BigDecimal.valueOf(1.2471),
                "AUD", BigDecimal.valueOf(2.9611),
                "BGN", BigDecimal.valueOf(2.5291)
        );
    }
}

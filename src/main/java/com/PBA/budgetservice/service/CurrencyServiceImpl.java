package com.PBA.budgetservice.service;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CurrencyServiceImpl implements CurrencyService {
    private final WebClient webClient;
    private final Logger logger = LoggerFactory.getLogger(CurrencyServiceImpl.class);

    public CurrencyServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Map<String, BigDecimal> getCurrencyExchangeRatesMapping() {
        String xmlSource = "https://www.bnr.ro/nbrfxrates.xml";
        Mono<String> responseMono = webClient.get()
                .uri(xmlSource)
                .retrieve()
                .bodyToMono(String.class);
        String content = responseMono.block();

        return this.getCurrencyMappingFromXmlString(content);
    }
    
    private Map<String, BigDecimal> getCurrencyMappingFromXmlString(String content) {
        SAXBuilder sax = new SAXBuilder();

        String fileName = "bnr_rates.xml";
        URI filePath = this.getUriOf(fileName);
        Path path = Path.of(filePath);
        this.writeToFile(path, content);

        try {
            Document doc = sax.build(new File(filePath.getPath()));
            Element rootNode = doc.getRootElement();
            Element bodyNode = rootNode.getChildren().get(1);
            Element cubeNode = bodyNode.getChildren().get(2);
            List<Element> rateNodes = cubeNode.getChildren();
            return rateNodes.stream()
                    .collect(Collectors.toMap(
                            (rateNode) -> rateNode.getAttributeValue("currency"),
                            (rateNode) -> BigDecimal.valueOf(Double.parseDouble(rateNode.getText())))
                    );
        }
        catch(Exception e) {
            logger.error(String.format("Error when trying to build the document corresponding to the %s file, returning null", fileName), e);
            return null;
        }
    }

    private URI getUriOf(String file) {
        try {
            URI filePath = ClassLoader.getSystemResource(file).toURI();
            return filePath;
        }
        catch(URISyntaxException e) {
            logger.error(String.format("Error when trying to get uri of %s, returning null", file), e);
            return null;
        }
    }

    private void writeToFile(Path path, String content) {
        try {
            Files.writeString(path, content, StandardCharsets.UTF_8);
        }
        catch(IOException e) {
            logger.error("Error when trying to write to file", e);
        }
    }
}

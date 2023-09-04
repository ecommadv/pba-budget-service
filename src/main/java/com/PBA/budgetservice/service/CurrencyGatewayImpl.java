package com.PBA.budgetservice.service;

import com.PBA.budgetservice.utils.XmlUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
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
public class CurrencyGatewayImpl implements CurrencyGateway {
    private final WebClient webClient;
    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyGatewayImpl.class);

    @Value("${currency.rates.url}")
    private String exchangeRatesXmlSource;

    public CurrencyGatewayImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Map<String, BigDecimal> getCurrencyExchangeRatesMapping() {
        Mono<String> responseMono = webClient.get()
                .uri(exchangeRatesXmlSource)
                .retrieve()
                .bodyToMono(String.class);
        String content = responseMono.block();

        return this.getCurrencyMappingFromXmlString(content);
    }
    
    private Map<String, BigDecimal> getCurrencyMappingFromXmlString(String content) {
        SAXBuilder sax = new SAXBuilder();

        try {
            Document doc = sax.build(new StringReader(content));
            Element rootNode = doc.getRootElement();
            Element bodyNode = XmlUtils.getElementByName(XmlUtils.BODY, rootNode);
            Element cubeNode = XmlUtils.getElementByName(XmlUtils.CUBE, bodyNode);
            List<Element> rateNodes = cubeNode.getChildren();
            return rateNodes.stream()
                    .collect(Collectors.toMap(
                            (rateNode) -> rateNode.getAttributeValue(XmlUtils.CURRENCY),
                            (rateNode) -> new BigDecimal(rateNode.getText()))
                    );
        }
        catch(Exception e) {
            LOGGER.error("Error when trying to build the document, returning empty map", e);
            return Map.of();
        }
    }
}

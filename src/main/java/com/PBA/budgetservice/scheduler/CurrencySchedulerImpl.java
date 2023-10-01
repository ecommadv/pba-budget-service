package com.PBA.budgetservice.scheduler;

import com.PBA.budgetservice.gateway.CurrencyGateway;
import com.PBA.budgetservice.gateway.CurrencyGatewayImpl;
import com.PBA.budgetservice.persistance.model.CurrencyRate;
import com.PBA.budgetservice.service.CurrencyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.List;

@Service
public class CurrencySchedulerImpl implements CurrencyScheduler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyGatewayImpl.class);
    private final CurrencyGateway currencyGateway;
    private final CurrencyService currencyService;

    public CurrencySchedulerImpl(CurrencyGateway currencyGateway, CurrencyService currencyService) {
        this.currencyGateway = currencyGateway;
        this.currencyService = currencyService;
    }

    @Override
    @Scheduled(cron = "${scheduler.currency_rate_cron}")
    @Transactional
    public void reloadCurrencyRates() {
        LOGGER.info("Currency rates reload scheduler triggered at {}", LocalDateTime.now());

        currencyService.deleteAllCurrencyRates();
        Map<String, BigDecimal> exchangeRatesMapping = currencyGateway.getCurrencyExchangeRatesMapping();
        List<CurrencyRate> currencyRates = this.getListOfCurrencyRatesFromMapping(exchangeRatesMapping);
        currencyRates.forEach(currencyService::addCurrencyRate);
    }

    private List<CurrencyRate> getListOfCurrencyRatesFromMapping(Map<String, BigDecimal> exchangeRatesMapping) {
        return exchangeRatesMapping
                .entrySet()
                .stream()
                .map(this::buildCurrencyRate)
                .toList();
    }

    private CurrencyRate buildCurrencyRate(Map.Entry<String, BigDecimal> entry) {
        String code = entry.getKey();
        BigDecimal ronValue = entry.getValue();
        return CurrencyRate.builder()
                .code(code)
                .mainValue(ronValue)
                .build();
    }
}

package com.PBA.budgetservice.persistance.repository;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component
public class UtilsFactoryImpl implements UtilsFactory {
    public KeyHolder keyHolder() {
        return new GeneratedKeyHolder();
    }
}

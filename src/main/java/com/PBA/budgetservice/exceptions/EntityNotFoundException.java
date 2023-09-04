package com.PBA.budgetservice.exceptions;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends BudgetException {
    public EntityNotFoundException(String message, String errorCode) {
        super(message, errorCode, HttpStatus.NOT_FOUND);
    }
}

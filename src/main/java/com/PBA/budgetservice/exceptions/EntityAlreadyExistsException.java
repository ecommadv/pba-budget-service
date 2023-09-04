package com.PBA.budgetservice.exceptions;

import org.springframework.http.HttpStatus;

public class EntityAlreadyExistsException extends BudgetException {
    public EntityAlreadyExistsException(String message, String errorCode) {
        super(message, errorCode, HttpStatus.BAD_REQUEST);
    }
}

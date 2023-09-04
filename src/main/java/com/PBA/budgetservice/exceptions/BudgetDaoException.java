package com.PBA.budgetservice.exceptions;

import org.springframework.http.HttpStatus;

public class BudgetDaoException extends BudgetException {
    public BudgetDaoException(String message, String errorCode, HttpStatus httpStatus) {
        super(message, errorCode, httpStatus);
    }
}

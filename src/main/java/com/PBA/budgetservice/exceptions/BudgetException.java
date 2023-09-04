package com.PBA.budgetservice.exceptions;

import org.springframework.http.HttpStatus;

public class BudgetException extends RuntimeException {
    private String errorCode;
    private HttpStatus httpStatus;

    public BudgetException(String message, String errorCode, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

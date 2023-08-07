package com.PBA.budgetservice.exceptions;

public class BudgetValidationException extends RuntimeException {
    public BudgetValidationException(String message) {
        super(message);
    }
}

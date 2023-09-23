package com.PBA.budgetservice.exceptions;

import org.springframework.http.HttpStatus;

public class AuthorizationException extends BudgetException {
    public AuthorizationException() {
        super("", "", HttpStatus.UNAUTHORIZED);
    }
}

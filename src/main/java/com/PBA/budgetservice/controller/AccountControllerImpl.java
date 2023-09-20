package com.PBA.budgetservice.controller;

import com.PBA.budgetservice.controller.request.AccountCreateRequest;
import com.PBA.budgetservice.facade.AccountFacade;
import com.PBA.budgetservice.persistance.model.dtos.AccountDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class AccountControllerImpl implements AccountController {
    private final AccountFacade accountFacade;

    public AccountControllerImpl(AccountFacade accountFacade) {
        this.accountFacade = accountFacade;
    }

    @Override
    public ResponseEntity<AccountDto> createAccount(AccountCreateRequest accountCreateRequest, String authHeader) {
        AccountDto accountDto = accountFacade.createAccount(accountCreateRequest, authHeader);
        return new ResponseEntity<>(accountDto, HttpStatus.CREATED);
    }
}

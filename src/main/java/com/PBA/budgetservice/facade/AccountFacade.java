package com.PBA.budgetservice.facade;

import com.PBA.budgetservice.controller.request.AccountCreateRequest;
import com.PBA.budgetservice.persistance.model.dtos.AccountDto;

public interface AccountFacade {
    public AccountDto createAccount(AccountCreateRequest accountCreateRequest);
}

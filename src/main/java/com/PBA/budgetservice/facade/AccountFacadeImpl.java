package com.PBA.budgetservice.facade;

import com.PBA.budgetservice.controller.request.AccountCreateRequest;
import com.PBA.budgetservice.exceptions.EntityAlreadyExistsException;
import com.PBA.budgetservice.exceptions.ErrorCodes;
import com.PBA.budgetservice.mapper.AccountMapper;
import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.dtos.AccountDto;
import com.PBA.budgetservice.service.AccountService;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AccountFacadeImpl implements AccountFacade {
    private final AccountService accountService;
    private final AccountMapper accountMapper;

    public AccountFacadeImpl(AccountService accountService, AccountMapper accountMapper) {
        this.accountService = accountService;
        this.accountMapper = accountMapper;
    }

    @Override
    public AccountDto createAccount(AccountCreateRequest accountCreateRequest) {
        this.validateAccountAlreadyExists(accountCreateRequest);
        Account accountToCreate = accountMapper.toAccount(accountCreateRequest);

        Account savedAccount = accountService.addAccount(accountToCreate);
        return accountMapper.toAccountDto(savedAccount);
    }

    private void validateAccountAlreadyExists(AccountCreateRequest accountCreateRequest) {
        UUID userUid = accountCreateRequest.getUserUid();
        String currency = accountCreateRequest.getCurrency();
        if (accountService.accountExists(userUid, currency)) {
            throw new EntityAlreadyExistsException(
                    String.format("Account with user uid %s and currency %s already exists", userUid, currency),
                    ErrorCodes.ACCOUNT_ALREADY_EXISTS
            );
        }
    }
}

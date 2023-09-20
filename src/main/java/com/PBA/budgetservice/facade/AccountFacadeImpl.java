package com.PBA.budgetservice.facade;

import com.PBA.budgetservice.controller.request.AccountCreateRequest;
import com.PBA.budgetservice.exceptions.EntityAlreadyExistsException;
import com.PBA.budgetservice.exceptions.ErrorCodes;
import com.PBA.budgetservice.gateway.UserGateway;
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
    private final UserGateway userGateway;

    public AccountFacadeImpl(AccountService accountService, AccountMapper accountMapper, UserGateway userGateway) {
        this.accountService = accountService;
        this.accountMapper = accountMapper;
        this.userGateway = userGateway;
    }

    @Override
    public AccountDto createAccount(AccountCreateRequest accountCreateRequest, String authHeader) {
        UUID userUid = userGateway.getUserUidFromAuthHeader(authHeader);
        this.validateAccountAlreadyExists(accountCreateRequest, userUid);
        Account accountToCreate = accountMapper.toAccount(accountCreateRequest, userUid);

        Account savedAccount = accountService.addAccount(accountToCreate);
        return accountMapper.toAccountDto(savedAccount);
    }

    private void validateAccountAlreadyExists(AccountCreateRequest accountCreateRequest, UUID userUid) {
        String currency = accountCreateRequest.getCurrency();
        if (accountService.accountExists(userUid, currency)) {
            throw new EntityAlreadyExistsException(
                    String.format("Account with user uid %s and currency %s already exists", userUid, currency),
                    ErrorCodes.ACCOUNT_ALREADY_EXISTS
            );
        }
    }
}

package com.PBA.budgetservice.facade;

import com.PBA.budgetservice.controller.request.AccountCreateRequest;
import com.PBA.budgetservice.exceptions.EntityAlreadyExistsException;
import com.PBA.budgetservice.exceptions.EntityNotFoundException;
import com.PBA.budgetservice.exceptions.ErrorCodes;
import com.PBA.budgetservice.gateway.UserGateway;
import com.PBA.budgetservice.mapper.AccountMapper;
import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.dtos.AccountDto;
import com.PBA.budgetservice.service.AccountService;
import com.PBA.budgetservice.service.CurrencyService;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AccountFacadeImpl implements AccountFacade {
    private final AccountService accountService;
    private final AccountMapper accountMapper;
    private final UserGateway userGateway;
    private final CurrencyService currencyService;

    public AccountFacadeImpl(AccountService accountService, AccountMapper accountMapper, UserGateway userGateway, CurrencyService currencyService) {
        this.accountService = accountService;
        this.accountMapper = accountMapper;
        this.userGateway = userGateway;
        this.currencyService = currencyService;
    }

    @Override
    public AccountDto createAccount(AccountCreateRequest accountCreateRequest, String authHeader) {
        UUID userUid = userGateway.getUserUidFromAuthHeader(authHeader);
        this.validateCurrencyCodeExists(accountCreateRequest.getCurrency());
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

    private void validateCurrencyCodeExists(String code) {
        if (!currencyService.currencyRateWithCodeExists(code)) {
            throw new EntityNotFoundException(
                    String.format("Currency rate with code %s does not exist", code),
                    ErrorCodes.CURRENCY_RATE_NOT_FOUND
            );
        }
    }
}

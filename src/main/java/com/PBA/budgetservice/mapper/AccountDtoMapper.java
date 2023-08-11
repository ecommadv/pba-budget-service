package com.PBA.budgetservice.mapper;

import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.controller.request.AccountRequest;
import com.PBA.budgetservice.persistance.model.dtos.AccountDto;
import org.mapstruct.Mapper;

@Mapper
public interface AccountDtoMapper {
    public Account fromAccountRequestToAccount(AccountRequest accountRequest);

    public AccountDto fromAccountToAccountResponse(Account accountResult);
}

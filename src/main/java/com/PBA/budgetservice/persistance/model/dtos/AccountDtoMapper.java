package com.PBA.budgetservice.persistance.model.dtos;

import com.PBA.budgetservice.persistance.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AccountDtoMapper {
    public Account fromAccountRequestToAccount(AccountRequest accountRequest);

    public AccountResponse fromAccountToAccountResponse(Account accountResult);
}

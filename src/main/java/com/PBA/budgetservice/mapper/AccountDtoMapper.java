package com.PBA.budgetservice.mapper;

import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.dtos.AccountDto;
import com.PBA.budgetservice.controller.request.AccountRequest;
import org.mapstruct.Mapper;

@Mapper
public interface AccountDtoMapper {
    public Account toAccount(AccountRequest accountRequest);

    public AccountDto toAccountDto(Account accountResult);
}

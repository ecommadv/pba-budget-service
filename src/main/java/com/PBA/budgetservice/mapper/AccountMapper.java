package com.PBA.budgetservice.mapper;

import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.dtos.AccountDto;
import com.PBA.budgetservice.controller.request.AccountCreateRequest;
import org.mapstruct.Mapper;

@Mapper
public interface AccountMapper {
    public Account toAccount(AccountCreateRequest accountRequest);

    public AccountDto toAccountDto(Account account);
}

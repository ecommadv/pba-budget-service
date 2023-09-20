package com.PBA.budgetservice.mapper;

import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.dtos.AccountDto;
import com.PBA.budgetservice.controller.request.AccountCreateRequest;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper
public interface AccountMapper {
    public Account toAccount(AccountCreateRequest accountRequest, UUID userUid);

    public AccountDto toAccountDto(Account account);
}

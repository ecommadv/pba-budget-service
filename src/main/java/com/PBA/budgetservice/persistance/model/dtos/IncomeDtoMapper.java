package com.PBA.budgetservice.persistance.model.dtos;

import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.Income;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface IncomeDtoMapper {
    public Income toIncome(IncomeRequest incomeRequest);
    public Account toAccount(IncomeRequest incomeRequest);
}

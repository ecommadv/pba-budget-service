package com.PBA.budgetservice.mapper;

import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.Income;
import com.PBA.budgetservice.persistance.model.dtos.IncomeDto;
import com.PBA.budgetservice.controller.request.IncomeCreateRequest;
import com.PBA.budgetservice.controller.request.IncomeUpdateRequest;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Map;

@Mapper
public interface IncomeDtoMapper {
    @Mapping(target = "uid", expression = "java(java.util.UUID.randomUUID())")
    public Income toIncome(IncomeCreateRequest incomeRequest);

    @Mapping(target = "amount",
            expression = "java(incomeUpdateRequest.getAmount() == null ? income.getAmount() : incomeUpdateRequest.getAmount())")
    @Mapping(target = "description",
            expression = "java(incomeUpdateRequest.getDescription() == null ? income.getDescription() : incomeUpdateRequest.getDescription())")
    public Income toIncome(@Context IncomeUpdateRequest incomeUpdateRequest, Income income);

    public Account toAccount(IncomeCreateRequest incomeRequest);

    @Mapping(target = "categoryName", expression = "java(categoryIdToNameMapping.get(income.getCategoryId()))")
    public IncomeDto toIncomeDto(Income income,
                                 @Context Map<Long, String> categoryIdToNameMapping);

    @Mapping(target = "categoryName", expression = "java(categoryName)")
    public IncomeDto toIncomeDto(Income income, String categoryName);

    public List<IncomeDto> toIncomeDto(List<Income> incomes,
                                       @Context Map<Long, String> categoryIdToNameMapping);
}

package com.PBA.budgetservice.persistance.model.dtos;

import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.Income;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Map;

@Mapper
public interface IncomeDtoMapper {
    @Mapping(target = "uid", expression = "java(java.util.UUID.randomUUID())")
    public Income toIncome(IncomeRequest incomeRequest);

    @Mapping(target = "amount",
            expression = "java(incomeUpdateRequest.getAmount() == null ? income.getAmount() : incomeUpdateRequest.getAmount())")
    @Mapping(target = "description",
            expression = "java(incomeUpdateRequest.getDescription() == null ? income.getDescription() : incomeUpdateRequest.getDescription())")
    public Income toIncome(@Context IncomeUpdateRequest incomeUpdateRequest, Income income);

    public Account toAccount(IncomeRequest incomeRequest);

    @Mapping(target = "categoryName", expression = "java(categoryIdToNameMapping.get(income.getCategoryId()))")
    public IncomeDto toIncomeResponse(Income income,
                                      @Context Map<Long, String> categoryIdToNameMapping);

    @Mapping(target = "categoryName", expression = "java(categoryName)")
    public IncomeDto toIncomeResponse(Income income, String categoryName);

    public List<IncomeDto> toIncomeResponse(List<Income> incomes,
                                            @Context Map<Long, String> categoryIdToNameMapping);
}

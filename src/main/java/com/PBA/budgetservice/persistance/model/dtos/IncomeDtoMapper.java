package com.PBA.budgetservice.persistance.model.dtos;

import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.Income;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    @Mapping(target = "userUid", expression = "java(mappingContext.getFirst().get(income.getAccountId()))")
    @Mapping(target = "categoryUid", expression = "java(mappingContext.getSecond().get(income.getCategoryId()))")
    public IncomeResponse toIncomeResponse(Income income,
                                           @Context Pair<Map<Long, UUID>, Map<Long, UUID>> mappingContext);

    public List<IncomeResponse> toIncomeResponse(List<Income> incomes,
                                                 @Context Pair<Map<Long, UUID>, Map<Long, UUID>> mappingContext);
}

package com.PBA.budgetservice.mapper;

import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.Income;
import com.PBA.budgetservice.persistance.model.IncomeCategory;
import com.PBA.budgetservice.persistance.model.dtos.IncomeCategoryDto;
import com.PBA.budgetservice.persistance.model.dtos.IncomeDto;
import com.PBA.budgetservice.controller.request.IncomeCreateRequest;
import com.PBA.budgetservice.controller.request.IncomeUpdateRequest;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mapper
public interface IncomeMapper {
    @Mapping(target = "uid", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "repetition", expression = "java(incomeRequest.getRepetition() == null " +
            "? com.PBA.budgetservice.persistance.model.Repetition.NONE " +
            ": incomeRequest.getRepetition())")
    public Income toIncome(IncomeCreateRequest incomeRequest);

    @Mapping(target = "amount",
            expression = "java(incomeUpdateRequest.getAmount() == null ? income.getAmount() : incomeUpdateRequest.getAmount())")
    @Mapping(target = "description",
            expression = "java(incomeUpdateRequest.getDescription() == null ? income.getDescription() : incomeUpdateRequest.getDescription())")
    @Mapping(target = "createdAt", expression = "java(incomeUpdateRequest.getCreatedAt())")
    @Mapping(target = "repetition", expression = "java(incomeUpdateRequest.getRepetition() == null " +
            "? income.getRepetition() " +
            ": incomeUpdateRequest.getRepetition())")
    public Income toIncome(@Context IncomeUpdateRequest incomeUpdateRequest, Income income);

    public Account toAccount(IncomeCreateRequest incomeRequest, UUID userUid);

    @Mapping(target = "categoryName", expression = "java(categoryIdToNameMapping.get(income.getCategoryId()))")
    public IncomeDto toIncomeDto(Income income,
                                 @Context Map<Long, String> categoryIdToNameMapping);

    @Mapping(target = "categoryName", expression = "java(categoryName)")
    public IncomeDto toIncomeDto(Income income, String categoryName);

    public List<IncomeDto> toIncomeDto(List<Income> incomes,
                                       @Context Map<Long, String> categoryIdToNameMapping);

    public IncomeCategoryDto toIncomeCategoryDto(IncomeCategory incomeCategory);

    public List<IncomeCategoryDto> toIncomeCategoryDto(List<IncomeCategory> incomeCategories);

    @Mapping(target = "uid", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "accountId", expression = "java(account.getId())")
    @Mapping(target = "categoryId", expression = "java(category.getId())")
    public Income toIncome(IncomeCategory incomeCategory, @Context Account account, @Context IncomeCategory category);

    @Mapping(target = "uid", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "repetition", expression = "java(com.PBA.budgetservice.persistance.model.Repetition.NONE)")
    public Income toScheduledIncome(Income income);
}

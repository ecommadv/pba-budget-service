package com.PBA.budgetservice.mapper;

import com.PBA.budgetservice.controller.request.ExpenseCreateRequest;
import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.Expense;
import com.PBA.budgetservice.persistance.model.dtos.ExpenseDto;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ExpenseMapper {
    public Account toAccount(ExpenseCreateRequest expenseCreateRequest);

    @Mapping(target = "uid", expression = "java(java.util.UUID.randomUUID())")
    public Expense toExpense(ExpenseCreateRequest expenseCreateRequest);

    @Mapping(target = "categoryName", expression = "java(categoryName)")
    public ExpenseDto toExpenseDto(Expense expense, String categoryName);
}

package com.PBA.budgetservice.mapper;

import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.Expense;
import com.PBA.budgetservice.persistance.model.ExpenseCategory;
import com.PBA.budgetservice.persistance.model.dtos.ExpenseCategoryDto;
import com.PBA.budgetservice.persistance.model.dtos.ExpenseDto;
import com.PBA.budgetservice.controller.request.ExpenseCreateRequest;
import com.PBA.budgetservice.controller.request.ExpenseUpdateRequest;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mapper
public interface ExpenseMapper {
    public Account toAccount(ExpenseCreateRequest expenseCreateRequest, UUID userUid);

    @Mapping(target = "uid", expression = "java(java.util.UUID.randomUUID())")
    public Expense toExpense(ExpenseCreateRequest expenseCreateRequest);

    @Mapping(target = "categoryName", expression = "java(categoryName)")
    public ExpenseDto toExpenseDto(Expense expense, String categoryName);

    @Mapping(target = "amount", expression = "java(expenseUpdateRequest.getAmount() == null ? expense.getAmount() : expenseUpdateRequest.getAmount())")
    @Mapping(target = "name", expression = "java(expenseUpdateRequest.getName() == null ? expense.getName() : expenseUpdateRequest.getName())")
    @Mapping(target = "description", expression = "java(expenseUpdateRequest.getDescription() == null ? expense.getDescription() : expenseUpdateRequest.getDescription())")
    @Mapping(target = "categoryId", expression = "java(expenseCategory.getId())")
    @Mapping(target = "createdAt", expression = "java(expenseUpdateRequest.getCreatedAt())")
    public Expense toExpense(@Context ExpenseUpdateRequest expenseUpdateRequest, Expense expense, @Context ExpenseCategory expenseCategory);

    @Mapping(target = "categoryName", expression = "java(categoryIdToNameMapping.get(expense.getCategoryId()))")
    public ExpenseDto toExpenseDto(Expense expense, @Context Map<Long, String> categoryIdToNameMapping);

    public List<ExpenseDto> toExpenseDto(List<Expense> expenses, @Context Map<Long, String> categoryIdToNameMapping);

    public ExpenseCategoryDto toExpenseCategoryDto(ExpenseCategory expenseCategory);

    public List<ExpenseCategoryDto> toExpenseCategoryDto(List<ExpenseCategory> expenseCategories);

    @Mapping(target = "uid", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "accountId", expression = "java(account.getId())")
    @Mapping(target = "categoryId", expression = "java(expenseCategory.getId())")
    public Expense toExpense(ExpenseCreateRequest expenseCreateRequest, @Context Account account, @Context ExpenseCategory expenseCategory);
}

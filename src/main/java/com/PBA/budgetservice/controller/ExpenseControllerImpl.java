package com.PBA.budgetservice.controller;

import com.PBA.budgetservice.facade.ExpenseFacade;
import com.PBA.budgetservice.persistance.model.dtos.ExpenseCategoryDto;
import com.PBA.budgetservice.persistance.model.dtos.ExpenseDto;
import com.PBA.budgetservice.controller.request.ExpenseCreateRequest;
import com.PBA.budgetservice.controller.request.ExpenseUpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
public class ExpenseControllerImpl implements ExpenseController {
    private final ExpenseFacade expenseFacade;

    public ExpenseControllerImpl(ExpenseFacade expenseFacade) {
        this.expenseFacade = expenseFacade;
    }

    @Override
    public ResponseEntity<ExpenseDto> createExpense(ExpenseCreateRequest expenseCreateRequest) {
        ExpenseDto expenseDto = expenseFacade.addExpense(expenseCreateRequest);
        return new ResponseEntity<>(expenseDto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ExpenseDto> updateExpense(ExpenseUpdateRequest expenseUpdateRequest, UUID uid) {
        ExpenseDto expenseDto = expenseFacade.updateExpense(expenseUpdateRequest, uid);
        return new ResponseEntity<>(expenseDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<ExpenseDto>> getAllExpensesByUserAndCurrency(String currency) {
        List<ExpenseDto> expenseDtos = expenseFacade.getAllExpensesByUserAndCurrency(currency);
        return new ResponseEntity<>(expenseDtos, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteExpense(UUID uid) {
        expenseFacade.deleteExpenseByUid(uid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<ExpenseCategoryDto>> getAllExpenseCategories() {
        List<ExpenseCategoryDto> expenseCategoryDtos = expenseFacade.getAllExpenseCategories();
        return new ResponseEntity<>(expenseCategoryDtos, HttpStatus.OK);
    }
}

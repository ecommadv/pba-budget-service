package com.PBA.budgetservice.controller;

import com.PBA.budgetservice.persistance.model.Expense;
import com.PBA.budgetservice.persistance.model.dtos.ExpenseCategoryDto;
import com.PBA.budgetservice.persistance.model.dtos.ExpenseDto;
import com.PBA.budgetservice.controller.request.ExpenseCreateRequest;
import com.PBA.budgetservice.controller.request.ExpenseUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/expense")
public interface ExpenseController {
    @Operation(summary = "Creates an expense and persists it in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created")
    })
    @PostMapping
    public ResponseEntity<ExpenseDto> createExpense(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Expense to create")
            @RequestBody ExpenseCreateRequest expenseCreateRequest);

    @Operation(summary = "Updates an expense and persists the changes in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PutMapping("/{uid}")
    public ResponseEntity<ExpenseDto> updateExpense(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Income to update")
                                                    @RequestBody ExpenseUpdateRequest expenseUpdateRequest,
                                                    @PathVariable("uid") UUID uid);
    @Operation(summary = "Provides a list of all the expenses with the specified user uid and currency that are currently stored in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @GetMapping
    public ResponseEntity<List<ExpenseDto>> getAllExpensesByUserUidAndCurrency(@RequestParam(name = "userUid") UUID userUid,
                                                                               @RequestParam(name = "currency") String currency);

    @Operation(summary = "Deletes the expense with the given uid from the system, if it exists.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @DeleteMapping("/{uid}")
    public ResponseEntity<Void> deleteExpense(@PathVariable("uid") UUID uid);

    @Operation(summary = "Provides a list of all the expense categories that are currently stored in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping("/category")
    public ResponseEntity<List<ExpenseCategoryDto>> getAllExpenseCategories();
}

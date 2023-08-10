package com.PBA.budgetservice.controller;

import com.PBA.budgetservice.persistance.model.dtos.IncomeRequest;
import com.PBA.budgetservice.persistance.model.dtos.IncomeResponse;
import com.PBA.budgetservice.persistance.model.dtos.IncomeUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budget")
public interface BudgetController {
    @Operation(summary = """
            Creates an income corresponding to the user uid specified in an IncomeRequest request body.
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created")
    })
    @PostMapping("/income")
    public ResponseEntity<Void> createIncome(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Income to create")
            @RequestBody IncomeRequest incomeRequest);

    @Operation(summary = """
            Provides a list of IncomeResponse entities that contain all the data of all the incomes currently stored in the application.
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping("/income")
    public ResponseEntity<List<IncomeResponse>> getAllIncomes();

    @Operation(summary = """
            Updates an income with the uid and attributes specified in an IncomeUpdateRequest request body.
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PutMapping("/income")
    public ResponseEntity<Void> updateIncome(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Income to update")
            @RequestBody IncomeUpdateRequest incomeUpdateRequest);
}
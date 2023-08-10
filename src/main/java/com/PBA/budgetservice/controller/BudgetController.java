package com.PBA.budgetservice.controller;

import com.PBA.budgetservice.persistance.model.dtos.IncomeRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
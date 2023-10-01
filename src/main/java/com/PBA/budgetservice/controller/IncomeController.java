package com.PBA.budgetservice.controller;

import com.PBA.budgetservice.controller.request.DateRange;
import com.PBA.budgetservice.controller.request.IncomeCreateRequest;
import com.PBA.budgetservice.controller.request.IncomeUpdateRequest;
import com.PBA.budgetservice.persistance.model.dtos.IncomeCategoryDto;
import com.PBA.budgetservice.persistance.model.dtos.IncomeDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/income")
public interface IncomeController {
    @Operation(summary = "Creates an income and persists it in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created")
    })
    @PostMapping
    public ResponseEntity<IncomeDto> createIncome(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Income to create")
            @Valid @RequestBody IncomeCreateRequest incomeRequest);

    @Operation(summary = "Updates an income and persists the changes in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PutMapping("/{uid}")
    public ResponseEntity<IncomeDto> updateIncome(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Income to update")
            @Valid @RequestBody IncomeUpdateRequest incomeUpdateRequest, @PathVariable("uid") UUID uid);

    @Operation(summary = "Deletes the income with the given uid from the system, if it exists.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @DeleteMapping("/{uid}")
    public ResponseEntity<Void> deleteIncome(@PathVariable("uid") UUID uid);

    @Operation(summary = "Provides a list of all the income categories that are currently stored in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping("/category")
    public ResponseEntity<List<IncomeCategoryDto>> getAllIncomeCategories();

    @Operation(summary = "Provides a list of all the (filtered) incomes corresponding to the logged in user/group.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping
    public ResponseEntity<List<IncomeDto>> getAllUserIncomes(@RequestParam(name = "category-name", required = false) String categoryName,
                                                             @RequestParam(name = "currency", required = false) String currency,
                                                             DateRange dateRange);
}
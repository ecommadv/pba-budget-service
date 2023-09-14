package com.PBA.budgetservice.controller;

import com.PBA.budgetservice.controller.request.AccountCreateRequest;
import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.dtos.AccountDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public interface AccountController {
    @Operation(summary = "Creates an account and persists it in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PostMapping
    public ResponseEntity<AccountDto> createAccount(@Valid @RequestBody AccountCreateRequest accountCreateRequest);
}

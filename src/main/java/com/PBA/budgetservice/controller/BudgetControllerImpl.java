package com.PBA.budgetservice.controller;

import com.PBA.budgetservice.facade.IncomeFacade;
import com.PBA.budgetservice.persistance.model.dtos.IncomeRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;


@Controller
public class BudgetControllerImpl implements BudgetController {
    private final IncomeFacade incomeFacade;

    public BudgetControllerImpl(IncomeFacade incomeFacade) {
        this.incomeFacade = incomeFacade;
    }

    @Override
    public ResponseEntity<Void> createIncome(IncomeRequest incomeRequest) {
        incomeFacade.addIncome(incomeRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}

package com.PBA.budgetservice.controller;

import com.PBA.budgetservice.facade.IncomeFacade;
import com.PBA.budgetservice.persistance.model.dtos.IncomeRequest;
import com.PBA.budgetservice.persistance.model.dtos.IncomeResponse;
import com.PBA.budgetservice.persistance.model.dtos.IncomeUpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;


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

    @Override
    public ResponseEntity<List<IncomeResponse>> getAllIncomes() {
        List<IncomeResponse> incomeResponses = incomeFacade.getAllIncomes();
        return new ResponseEntity<>(incomeResponses, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> updateIncome(IncomeUpdateRequest updateIncomeRequest) {
        incomeFacade.updateIncome(updateIncomeRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

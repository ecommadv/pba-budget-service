package com.PBA.budgetservice.controller;

import com.PBA.budgetservice.facade.IncomeFacade;
import com.PBA.budgetservice.persistance.model.dtos.IncomeDto;
import com.PBA.budgetservice.persistance.model.dtos.IncomeRequest;
import com.PBA.budgetservice.persistance.model.dtos.IncomeUpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;


@Controller
public class IncomeControllerImpl implements IncomeController {
    private final IncomeFacade incomeFacade;

    public IncomeControllerImpl(IncomeFacade incomeFacade) {
        this.incomeFacade = incomeFacade;
    }

    @Override
    public ResponseEntity<IncomeDto> createIncome(IncomeRequest incomeRequest) {
        IncomeDto incomeResponse = incomeFacade.addIncome(incomeRequest);
        return new ResponseEntity<>(incomeResponse, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<IncomeDto>> getAllIncomes() {
        List<IncomeDto> incomeResponses = incomeFacade.getAllIncomes();
        return new ResponseEntity<>(incomeResponses, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<IncomeDto> updateIncome(IncomeUpdateRequest updateIncomeRequest, UUID uid) {
        IncomeDto incomeResponse = incomeFacade.updateIncome(updateIncomeRequest, uid);
        return new ResponseEntity<>(incomeResponse, HttpStatus.OK);
    }
}

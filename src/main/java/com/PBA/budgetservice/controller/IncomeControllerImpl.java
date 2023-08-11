package com.PBA.budgetservice.controller;

import com.PBA.budgetservice.facade.IncomeFacade;
import com.PBA.budgetservice.persistance.model.dtos.IncomeDto;
import com.PBA.budgetservice.controller.request.IncomeCreateRequest;
import com.PBA.budgetservice.controller.request.IncomeUpdateRequest;
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
    public ResponseEntity<IncomeDto> createIncome(IncomeCreateRequest incomeRequest) {
        IncomeDto incomeDto = incomeFacade.addIncome(incomeRequest);
        return new ResponseEntity<>(incomeDto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<IncomeDto>> getAllIncomes() {
        List<IncomeDto> incomeDtos = incomeFacade.getAllIncomes();
        return new ResponseEntity<>(incomeDtos, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<IncomeDto> updateIncome(IncomeUpdateRequest updateIncomeRequest, UUID uid) {
        IncomeDto incomeDto = incomeFacade.updateIncome(updateIncomeRequest, uid);
        return new ResponseEntity<>(incomeDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<IncomeDto> deleteIncome(UUID uid) {
        IncomeDto incomeDto = incomeFacade.deleteIncomeByUid(uid);
        return new ResponseEntity<>(incomeDto, HttpStatus.OK);
    }
}

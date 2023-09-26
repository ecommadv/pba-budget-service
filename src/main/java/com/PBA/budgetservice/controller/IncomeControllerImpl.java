package com.PBA.budgetservice.controller;

import com.PBA.budgetservice.controller.request.IncomeCreateRequest;
import com.PBA.budgetservice.controller.request.IncomeUpdateRequest;
import com.PBA.budgetservice.facade.IncomeFacade;
import com.PBA.budgetservice.persistance.model.dtos.IncomeCategoryDto;
import com.PBA.budgetservice.persistance.model.dtos.IncomeDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
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
    public ResponseEntity<List<IncomeDto>> getAllIncomesByUserAndCurrency(String currency) {
        List<IncomeDto> incomeDtos = incomeFacade.getAllIncomesByUserAndCurrency(currency);
        return new ResponseEntity<>(incomeDtos, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<IncomeDto> updateIncome(IncomeUpdateRequest updateIncomeRequest, UUID uid) {
        IncomeDto incomeDto = incomeFacade.updateIncome(updateIncomeRequest, uid);
        return new ResponseEntity<>(incomeDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteIncome(UUID uid) {
        incomeFacade.deleteIncomeByUid(uid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<IncomeCategoryDto>> getAllIncomeCategories() {
        List<IncomeCategoryDto> incomeCategoryDtos = incomeFacade.getAllIncomeCategories();
        return new ResponseEntity<>(incomeCategoryDtos, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<IncomeDto>> getAllUserIncomesByCategoryName(String categoryName) {
        List<IncomeDto> incomes = incomeFacade.getAllIncomesByUserAndCategoryName(categoryName);
        return new ResponseEntity<>(incomes, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<IncomeDto>> getAllUserIncomesByDate(LocalDateTime after, LocalDateTime before) {
        List<IncomeDto> incomes = incomeFacade.getAllIncomesByUserAndDate(after, before);
        return new ResponseEntity<>(incomes, HttpStatus.OK);
    }
}

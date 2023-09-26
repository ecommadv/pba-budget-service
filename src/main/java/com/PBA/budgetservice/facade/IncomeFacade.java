package com.PBA.budgetservice.facade;

import com.PBA.budgetservice.persistance.model.dtos.IncomeCategoryDto;
import com.PBA.budgetservice.persistance.model.dtos.IncomeDto;
import com.PBA.budgetservice.controller.request.IncomeCreateRequest;
import com.PBA.budgetservice.controller.request.IncomeUpdateRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface IncomeFacade {
    public IncomeDto addIncome(IncomeCreateRequest incomeRequest);
    public IncomeDto updateIncome(IncomeUpdateRequest incomeUpdateRequest, UUID uid);
    public void deleteIncomeByUid(UUID uid);
    public List<IncomeCategoryDto> getAllIncomeCategories();
    public List<IncomeDto> getAllIncomesByUserAndCurrency(String currency);
    public List<IncomeDto> getAllIncomesByUserAndCategoryName(String categoryName);
    public List<IncomeDto> getAllIncomesByUserAndDate(LocalDateTime after, LocalDateTime before);
}

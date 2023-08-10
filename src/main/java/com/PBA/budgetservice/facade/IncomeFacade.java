package com.PBA.budgetservice.facade;

import com.PBA.budgetservice.persistance.model.dtos.IncomeDto;
import com.PBA.budgetservice.persistance.model.dtos.IncomeRequest;
import com.PBA.budgetservice.persistance.model.dtos.IncomeUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface IncomeFacade {
    public IncomeDto addIncome(IncomeRequest incomeRequest);
    public List<IncomeDto> getAllIncomes();
    public IncomeDto updateIncome(IncomeUpdateRequest incomeUpdateRequest, UUID uid);
}

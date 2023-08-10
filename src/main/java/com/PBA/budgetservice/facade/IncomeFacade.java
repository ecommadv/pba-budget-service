package com.PBA.budgetservice.facade;

import com.PBA.budgetservice.persistance.model.dtos.IncomeRequest;
import com.PBA.budgetservice.persistance.model.dtos.IncomeResponse;
import com.PBA.budgetservice.persistance.model.dtos.IncomeUpdateRequest;

import java.util.List;

public interface IncomeFacade {
    public void addIncome(IncomeRequest incomeRequest);
    public List<IncomeResponse> getAllIncomes();
    public void updateIncome(IncomeUpdateRequest incomeUpdateRequest);
}

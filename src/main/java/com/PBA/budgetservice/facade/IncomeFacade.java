package com.PBA.budgetservice.facade;

import com.PBA.budgetservice.persistance.model.dtos.IncomeRequest;

public interface IncomeFacade {
    public void addIncome(IncomeRequest incomeRequest);
}

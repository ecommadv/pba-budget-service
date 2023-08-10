package com.PBA.budgetservice.service;

import com.PBA.budgetservice.persistance.model.Income;
import com.PBA.budgetservice.persistance.repository.IncomeDao;
import org.springframework.stereotype.Service;

@Service
public class IncomeServiceImpl implements IncomeService {
    private final IncomeDao incomeDao;

    public IncomeServiceImpl(IncomeDao incomeDao) {
        this.incomeDao = incomeDao;
    }

    @Override
    public Income addIncome(Income income) {
        return incomeDao.save(income);
    }
}

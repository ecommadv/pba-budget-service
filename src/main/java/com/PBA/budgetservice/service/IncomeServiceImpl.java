package com.PBA.budgetservice.service;

import com.PBA.budgetservice.exceptions.BudgetServiceException;
import com.PBA.budgetservice.persistance.model.Income;
import com.PBA.budgetservice.persistance.repository.IncomeDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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

    @Override
    public List<Income> getAllIncomes() {
        return incomeDao.getAll();
    }

    @Override
    public Income getIncomeByUid(UUID uid) {
        return incomeDao.getByUid(uid)
                .orElseThrow(() -> new BudgetServiceException(String.format("Income with uid %s does not exist!", uid.toString())));
    }

    @Override
    public Income updateIncome(Income income) {
        return incomeDao.update(income, income.getId());
    }

    @Override
    public Income deleteIncomeById(Long id) {
        return incomeDao.deleteById(id);
    }

    @Override
    public Income getIncomeById(Long id) {
        return incomeDao.getById(id)
                .orElseThrow(() -> new BudgetServiceException(String.format("Income with id %d does not exist!", id)));
    }
}

package com.PBA.budgetservice.service;

import com.PBA.budgetservice.exceptions.EntityNotFoundException;
import com.PBA.budgetservice.exceptions.ErrorCodes;
import com.PBA.budgetservice.persistance.model.Income;
import com.PBA.budgetservice.persistance.repository.IncomeDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

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
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Income with uid %s does not exist!", uid.toString()),
                        ErrorCodes.INCOME_NOT_FOUND));
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
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Income with id %d does not exist!", id),
                        ErrorCodes.INCOME_NOT_FOUND));
    }

    @Override
    public List<Income> getIncomeByAccountId(Long accountId) {
        return incomeDao.getAll()
                .stream()
                .filter(income -> Objects.equals(income.getAccountId(), accountId))
                .collect(Collectors.toList());
    }
}

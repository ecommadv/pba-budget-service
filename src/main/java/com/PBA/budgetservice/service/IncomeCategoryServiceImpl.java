package com.PBA.budgetservice.service;

import com.PBA.budgetservice.exceptions.BudgetServiceException;
import com.PBA.budgetservice.persistance.model.IncomeCategory;
import com.PBA.budgetservice.persistance.repository.IncomeCategoryDao;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class IncomeCategoryServiceImpl implements IncomeCategoryService {
    private final IncomeCategoryDao incomeCategoryDao;

    public IncomeCategoryServiceImpl(IncomeCategoryDao incomeCategoryDao) {
        this.incomeCategoryDao = incomeCategoryDao;
    }

    @Override
    public IncomeCategory getIncomeCategoryByUid(UUID uid) {
        return incomeCategoryDao.getIncomeCategoryByUid(uid)
                .orElseThrow(() -> new BudgetServiceException(String.format("Income category with uid %s does not exist!", uid.toString())));
    }

    @Override
    public Map<Long, String> getIncomeCategoryIdToNameMapping() {
        return incomeCategoryDao.getAll()
                .stream()
                .collect(Collectors.toMap(IncomeCategory::getId, IncomeCategory::getName));
    }

    @Override
    public IncomeCategory getIncomeCategoryById(Long id) {
        return incomeCategoryDao.getById(id)
                .orElseThrow(() -> new BudgetServiceException(String.format("Income category with uid %d does not exist!", id)));
    }
}

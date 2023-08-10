package com.PBA.budgetservice.service;

import com.PBA.budgetservice.exceptions.BudgetServiceException;
import com.PBA.budgetservice.persistance.model.IncomeCategory;
import com.PBA.budgetservice.persistance.repository.IncomeCategoryDao;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

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
}

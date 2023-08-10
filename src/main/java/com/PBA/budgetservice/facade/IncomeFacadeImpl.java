package com.PBA.budgetservice.facade;

import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.Income;
import com.PBA.budgetservice.persistance.model.IncomeCategory;
import com.PBA.budgetservice.persistance.model.dtos.IncomeDtoMapper;
import com.PBA.budgetservice.persistance.model.dtos.IncomeRequest;
import com.PBA.budgetservice.service.AccountService;
import com.PBA.budgetservice.service.IncomeCategoryService;
import com.PBA.budgetservice.service.IncomeService;
import org.springframework.stereotype.Component;

@Component
public class IncomeFacadeImpl implements IncomeFacade {
    private final IncomeService incomeService;
    private final AccountService accountService;
    private final IncomeCategoryService incomeCategoryService;
    private final IncomeDtoMapper incomeDtoMapper;

    public IncomeFacadeImpl(IncomeService incomeService, AccountService accountService, IncomeCategoryService incomeCategoryService, IncomeDtoMapper incomeDtoMapper) {
        this.incomeService = incomeService;
        this.accountService = accountService;
        this.incomeCategoryService = incomeCategoryService;
        this.incomeDtoMapper = incomeDtoMapper;
    }

    @Override
    public void addIncome(IncomeRequest incomeRequest) {
        Account account = incomeDtoMapper.toAccount(incomeRequest);
        Income income = incomeDtoMapper.toIncome(incomeRequest);

        Account savedAccount = accountService.addAccount(account);
        income.setAccountId(savedAccount.getId());

        IncomeCategory incomeCategory = incomeCategoryService.getIncomeCategoryByUid(incomeRequest.getCategoryUid());
        income.setCategoryId(incomeCategory.getId());

        incomeService.addIncome(income);
    }
}

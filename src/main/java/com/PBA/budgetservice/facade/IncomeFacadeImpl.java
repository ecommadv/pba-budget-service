package com.PBA.budgetservice.facade;

import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.Income;
import com.PBA.budgetservice.persistance.model.IncomeCategory;
import com.PBA.budgetservice.persistance.model.dtos.IncomeDto;
import com.PBA.budgetservice.persistance.model.dtos.IncomeDtoMapper;
import com.PBA.budgetservice.persistance.model.dtos.IncomeRequest;
import com.PBA.budgetservice.persistance.model.dtos.IncomeUpdateRequest;
import com.PBA.budgetservice.service.AccountService;
import com.PBA.budgetservice.service.IncomeCategoryService;
import com.PBA.budgetservice.service.IncomeService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    public IncomeDto addIncome(IncomeRequest incomeRequest) {
        Account account = incomeDtoMapper.toAccount(incomeRequest);
        Income income = incomeDtoMapper.toIncome(incomeRequest);

        Account savedAccount = accountService.addAccount(account);
        income.setAccountId(savedAccount.getId());

        IncomeCategory incomeCategory = incomeCategoryService.getIncomeCategoryByUid(incomeRequest.getCategoryUid());
        income.setCategoryId(incomeCategory.getId());

        Income savedIncome = incomeService.addIncome(income);
        return incomeDtoMapper.toIncomeResponse(savedIncome, incomeCategory.getName());
    }

    @Override
    public List<IncomeDto> getAllIncomes() {
        Map<Long, String> incomeCategoryIdToNameMapping = incomeCategoryService.getIncomeCategoryIdToNameMapping();
        return incomeDtoMapper.toIncomeResponse(incomeService.getAllIncomes(),
                                                incomeCategoryIdToNameMapping);
    }

    @Override
    public IncomeDto updateIncome(IncomeUpdateRequest incomeUpdateRequest, UUID uid) {
        Income incomeToUpdate = incomeService.getIncomeByUid(uid);
        IncomeCategory currentIncomeCategory = incomeCategoryService.getIncomeCategoryById(incomeToUpdate.getCategoryId());

        UUID categoryUid = incomeUpdateRequest.getCategoryUid() == null ? currentIncomeCategory.getUid() : incomeUpdateRequest.getCategoryUid();
        IncomeCategory incomeCategory = incomeCategoryService.getIncomeCategoryByUid(categoryUid);
        incomeToUpdate.setCategoryId(incomeCategory.getId());

        Income updatedIncome = incomeDtoMapper.toIncome(incomeUpdateRequest, incomeToUpdate);
        incomeService.updateIncome(updatedIncome);

        return incomeDtoMapper.toIncomeResponse(updatedIncome, incomeCategory.getName());
    }
}

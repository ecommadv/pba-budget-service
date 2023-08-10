package com.PBA.budgetservice.facade;

import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.Income;
import com.PBA.budgetservice.persistance.model.IncomeCategory;
import com.PBA.budgetservice.persistance.model.dtos.IncomeDtoMapper;
import com.PBA.budgetservice.persistance.model.dtos.IncomeRequest;
import com.PBA.budgetservice.persistance.model.dtos.IncomeResponse;
import com.PBA.budgetservice.persistance.model.dtos.IncomeUpdateRequest;
import com.PBA.budgetservice.service.AccountService;
import com.PBA.budgetservice.service.IncomeCategoryService;
import com.PBA.budgetservice.service.IncomeService;
import org.springframework.data.util.Pair;
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
    public void addIncome(IncomeRequest incomeRequest) {
        Account account = incomeDtoMapper.toAccount(incomeRequest);
        Income income = incomeDtoMapper.toIncome(incomeRequest);

        Account savedAccount = accountService.addAccount(account);
        income.setAccountId(savedAccount.getId());

        IncomeCategory incomeCategory = incomeCategoryService.getIncomeCategoryByUid(incomeRequest.getCategoryUid());
        income.setCategoryId(incomeCategory.getId());

        incomeService.addIncome(income);
    }

    @Override
    public List<IncomeResponse> getAllIncomes() {
        Map<Long, UUID> accountIdToUserUidMapping = accountService.getAccountIdToUserUidMapping();
        Map<Long, UUID> incomeCategoryIdToUidMapping = incomeCategoryService.getIncomeCategoryIdToUidMapping();
        return incomeDtoMapper.toIncomeResponse(incomeService.getAllIncomes(),
                                                Pair.of(accountIdToUserUidMapping, incomeCategoryIdToUidMapping));
    }

    @Override
    public void updateIncome(IncomeUpdateRequest incomeUpdateRequest) {
        Income incomeToUpdate = incomeService.getIncomeByUid(incomeUpdateRequest.getUid());
        Income updatedIncome = incomeDtoMapper.toIncome(incomeUpdateRequest, incomeToUpdate);
        incomeService.updateIncome(updatedIncome);
    }
}

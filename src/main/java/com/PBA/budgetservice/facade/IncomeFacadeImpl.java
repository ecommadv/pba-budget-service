package com.PBA.budgetservice.facade;

import com.PBA.budgetservice.exceptions.AuthorizationException;
import com.PBA.budgetservice.exceptions.EntityNotFoundException;
import com.PBA.budgetservice.exceptions.ErrorCodes;
import com.PBA.budgetservice.gateway.UserGateway;
import com.PBA.budgetservice.mapper.IncomeMapper;
import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.model.Expense;
import com.PBA.budgetservice.persistance.model.Income;
import com.PBA.budgetservice.persistance.model.IncomeCategory;
import com.PBA.budgetservice.persistance.model.dtos.IncomeCategoryDto;
import com.PBA.budgetservice.persistance.model.dtos.IncomeDto;
import com.PBA.budgetservice.security.JwtSecurityService;
import com.PBA.budgetservice.service.AccountService;
import com.PBA.budgetservice.service.CurrencyService;
import com.PBA.budgetservice.service.IncomeCategoryService;
import com.PBA.budgetservice.service.IncomeService;
import com.PBA.budgetservice.controller.request.IncomeCreateRequest;
import com.PBA.budgetservice.controller.request.IncomeUpdateRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class IncomeFacadeImpl implements IncomeFacade {
    private final IncomeService incomeService;
    private final AccountService accountService;
    private final IncomeCategoryService incomeCategoryService;
    private final IncomeMapper incomeMapper;
    private final CurrencyService currencyService;
    private final JwtSecurityService jwtSecurityService;

    public IncomeFacadeImpl(IncomeService incomeService, AccountService accountService, IncomeCategoryService incomeCategoryService, IncomeMapper incomeMapper, UserGateway userGateway, CurrencyService currencyService, JwtSecurityService jwtSecurityService) {
        this.incomeService = incomeService;
        this.accountService = accountService;
        this.incomeCategoryService = incomeCategoryService;
        this.incomeMapper = incomeMapper;
        this.currencyService = currencyService;
        this.jwtSecurityService = jwtSecurityService;
    }

    @Override
    public IncomeDto addIncome(IncomeCreateRequest incomeRequest) {
        this.validateCurrencyCodeExists(incomeRequest.getCurrency());
        UUID userUid = jwtSecurityService.getCurrentUserUid();
        Account account = incomeMapper.toAccount(incomeRequest, userUid);
        Income income = incomeMapper.toIncome(incomeRequest);

        Account savedAccount = accountService.addAccount(account);
        income.setAccountId(savedAccount.getId());

        IncomeCategory incomeCategory = incomeCategoryService.getIncomeCategoryByUid(incomeRequest.getCategoryUid());
        income.setCategoryId(incomeCategory.getId());

        Income savedIncome = incomeService.addIncome(income);
        return incomeMapper.toIncomeDto(savedIncome, incomeCategory.getName());
    }

    @Override
    public IncomeDto updateIncome(IncomeUpdateRequest incomeUpdateRequest, UUID uid) {
        UUID userUid = jwtSecurityService.getCurrentUserUid();
        Income incomeToUpdate = incomeService.getIncomeByUid(uid);
        this.validateIncomeIsOwnedByUser(incomeToUpdate, userUid);
        IncomeCategory currentIncomeCategory = incomeCategoryService.getIncomeCategoryById(incomeToUpdate.getCategoryId());

        UUID categoryUid = incomeUpdateRequest.getCategoryUid() == null ? currentIncomeCategory.getUid() : incomeUpdateRequest.getCategoryUid();
        IncomeCategory incomeCategory = incomeCategoryService.getIncomeCategoryByUid(categoryUid);
        incomeToUpdate.setCategoryId(incomeCategory.getId());

        Income updatedIncome = incomeMapper.toIncome(incomeUpdateRequest, incomeToUpdate);
        incomeService.updateIncome(updatedIncome);

        return incomeMapper.toIncomeDto(updatedIncome, incomeCategory.getName());
    }

    @Override
    public void deleteIncomeByUid(UUID uid) {
        UUID userUid = jwtSecurityService.getCurrentUserUid();
        Income incomeToDelete = incomeService.getIncomeByUid(uid);
        this.validateIncomeIsOwnedByUser(incomeToDelete, userUid);
        incomeService.deleteIncomeById(incomeToDelete.getId());
    }

    @Override
    public List<IncomeCategoryDto> getAllIncomeCategories() {
        List<IncomeCategory> incomeCategories = incomeCategoryService.getAllIncomeCategories();
        return incomeMapper.toIncomeCategoryDto(incomeCategories);
    }

    @Override
    public List<IncomeDto> getAllIncomesByUserAndCurrency(String currency) {
        UUID userUid = jwtSecurityService.getCurrentUserUid();
        Account account = accountService.getByUserUidAndCurrency(userUid, currency);
        List<Income> incomes = incomeService.getIncomeByAccountId(account.getId());

        Map<Long, String> categoryIdNameMapping = incomeCategoryService.getIncomeCategoryIdToNameMapping();
        return incomeMapper.toIncomeDto(incomes, categoryIdNameMapping);
    }

    @Override
    public List<IncomeDto> getAllIncomesByUserAndCategoryName(String categoryName) {
        UUID userUid = jwtSecurityService.getCurrentUserUid();
        List<Income> incomes = incomeService.getAllIncomesByUserUidAndCategoryName(userUid, categoryName);

        Map<Long, String> categoryIdNameMapping = incomeCategoryService.getIncomeCategoryIdToNameMapping();
        return incomeMapper.toIncomeDto(incomes, categoryIdNameMapping);
    }

    @Override
    public List<IncomeDto> getAllIncomesByUserAndDate(LocalDateTime after, LocalDateTime before) {
        if (after == null && before == null) {
            return List.of();
        }
        UUID userUid = jwtSecurityService.getCurrentUserUid();
        List<Income> incomes =
                after == null
                ? incomeService.getAllIncomesByUserUidAndDateBefore(userUid, before)
                : before == null
                    ? incomeService.getAllIncomesByUserUidAndDateAfter(userUid, after)
                : incomeService.getAllIncomesByUserUidAndDateBetween(userUid, after, before);

        Map<Long, String> categoryIdNameMapping = incomeCategoryService.getIncomeCategoryIdToNameMapping();
        return incomeMapper.toIncomeDto(incomes, categoryIdNameMapping);
    }

    private void validateCurrencyCodeExists(String code) {
        if (!currencyService.currencyRateWithCodeExists(code)) {
            throw new EntityNotFoundException(
                    String.format("Currency rate with code %s does not exist", code),
                    ErrorCodes.CURRENCY_RATE_NOT_FOUND
            );
        }
    }

    private void validateIncomeIsOwnedByUser(Income income, UUID userUid) {
        Account account = accountService.getById(income.getAccountId());
        if (!account.getUserUid().equals(userUid)) {
            throw new AuthorizationException();
        }
    }
}

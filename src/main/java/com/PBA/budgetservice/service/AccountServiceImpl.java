package com.PBA.budgetservice.service;

import com.PBA.budgetservice.exceptions.EntityNotFoundException;
import com.PBA.budgetservice.exceptions.ErrorCodes;
import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.repository.AccountDao;
import org.flywaydb.core.api.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountDao accountDao;

    @Autowired
    public AccountServiceImpl(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public Account addAccount(Account account) {
        Optional<Account> savedAccount = accountDao.getByUserUidAndCurrency(Pair.of(account.getUserUid(), account.getCurrency()));
        return savedAccount.orElseGet(() -> accountDao.save(account));
    }

    @Override
    public Account deleteAccountById(Long id) {
        return accountDao.deleteById(id);
    }

    @Override
    public Account getByUserUidAndCurrency(UUID userUid, String currency) {
        return accountDao.getByUserUidAndCurrency(Pair.of(userUid, currency))
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Account with user uid %s and currency %s does not exist",
                        userUid.toString(),
                        currency), ErrorCodes.ACCOUNT_NOT_FOUND)
                );
    }

    @Override
    public boolean accountExists(UUID userUid, String currency) {
        return accountDao.getByUserUidAndCurrency(Pair.of(userUid, currency)).isPresent();
    }

    @Override
    public Account getById(Long accountId) {
        return accountDao.getById(accountId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Account does not exist",
                        ErrorCodes.ACCOUNT_NOT_FOUND
                ));
    }
}

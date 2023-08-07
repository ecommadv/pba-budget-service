package com.PBA.budgetservice.service;

import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.repository.AccountDao;
import com.PBA.budgetservice.validators.AccountValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountDao accountDao;

    @Autowired
    public AccountServiceImpl(AccountDao accountDao, AccountValidator accountValidator) {
        this.accountDao = accountDao;
    }

    @Override
    public Account addAccount(Account account) {
        return accountDao.save(account);
    }
}

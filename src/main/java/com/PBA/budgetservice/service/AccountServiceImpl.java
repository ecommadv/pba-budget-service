package com.PBA.budgetservice.service;

import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.repository.AccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
}

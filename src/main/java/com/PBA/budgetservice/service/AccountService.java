package com.PBA.budgetservice.service;

import com.PBA.budgetservice.persistance.model.Account;

import java.util.UUID;

public interface AccountService {
    public Account addAccount(Account account);
    public Account deleteAccountById(Long id);
    public Account getByUserUidAndCurrency(UUID userUid, String currency);
    public boolean accountExists(UUID userUid, String currency);
}

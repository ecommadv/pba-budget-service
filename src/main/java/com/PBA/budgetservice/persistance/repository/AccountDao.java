package com.PBA.budgetservice.persistance.repository;

import com.PBA.budgetservice.persistance.model.Account;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

public interface AccountDao {
    public Account save(Account account);
    public Optional<Account> getById(Long id);
    public List<Account> getAll();
    public Account deleteById(Long id);
    public Account update(Account account, Long id);

}

package com.pba.budgetservice.integration;

import com.PBA.budgetservice.exceptions.BudgetDaoException;
import com.PBA.budgetservice.persistance.model.Account;
import com.PBA.budgetservice.persistance.repository.AccountDao;
import mockgenerators.AccountMockGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
public class AccountDaoIntegrationTest extends BaseDaoIntegrationTest {
    @Autowired
    private AccountDao accountDao;

    @Test
    public void testSave() {
        // given
        Account account = AccountMockGenerator.generateMockAccount();

        // when
        Account result = accountDao.save(account);

        // then
        Assertions.assertEquals(account.getUserUid(), result.getUserUid());
    }
    @Test
    public void testGetAll() {
        // given
        List<Account> accounts = AccountMockGenerator.generateMockListOfAccounts(10);
        this.addMockListOfAccounts(accounts);
        List<UUID> accountsUids = this.extractUids(accounts);

        // when
        List<Account> result = accountDao.getAll();
        List<UUID> resultUids = this.extractUids(result);

        // then
        Assertions.assertEquals(accountsUids, resultUids);
    }

    @Test
    public void testGetPresentById() {
        // given
        Account account = AccountMockGenerator.generateMockAccount();
        Account savedAccount = accountDao.save(account);

        // when
        Optional<Account> result = accountDao.getById(savedAccount.getId());

        // then
        Assertions.assertEquals(account.getUserUid(), result.get().getUserUid());
    }

    @Test
    public void testGetAbsentById() {
        // given
        Long id = new Random().nextLong();

        // when
        Optional<Account> result = accountDao.getById(id);

        // then
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testDeletePresentById() {
        // given
        Account account = AccountMockGenerator.generateMockAccount();
        Account savedAccount = accountDao.save(account);

        // when
        Account result = accountDao.deleteById(savedAccount.getId());

        // then
        Assertions.assertEquals(account.getUserUid(), result.getUserUid());
        Assertions.assertEquals(0, accountDao.getAll().size());
    }

    @Test
    public void testDeleteAbsentById() {
        Long id = new Random().nextLong();

        assertThatThrownBy(() -> accountDao.deleteById(id))
                .isInstanceOf(BudgetDaoException.class)
                .hasMessage(String.format("Object with id %s is not stored!", id.toString()));
    }

    @Test
    public void testUpdatePresent() {
        // given
        Account account = AccountMockGenerator.generateMockAccount();
        Account newAccount = AccountMockGenerator.generateMockAccount();
        Account savedAccount = accountDao.save(account);

        // when
        Account result = accountDao.update(newAccount, savedAccount.getId());

        // then
        Assertions.assertEquals(newAccount.getUserUid(), result.getUserUid());
        Assertions.assertEquals(newAccount.getUserUid(), accountDao.getById(savedAccount.getId()).get().getUserUid());
    }

    @Test
    public void testUpdateAbsent() {
        Account account = AccountMockGenerator.generateMockAccount();

        assertThatThrownBy(() -> accountDao.update(account, account.getId()))
                .isInstanceOf(BudgetDaoException.class)
                .hasMessage(String.format("Object with id %d is not stored!", account.getId()));
    }

    private void addMockListOfAccounts(List<Account> accounts) {
        for (Account account : accounts) {
            accountDao.save(account);
        }
    }

    private List<UUID> extractUids(List<Account> accounts) {
        return accounts.stream().map((Account::getUserUid)).collect(Collectors.toList());
    }
}

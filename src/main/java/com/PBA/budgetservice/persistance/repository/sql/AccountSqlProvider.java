package com.PBA.budgetservice.persistance.repository.sql;

public interface AccountSqlProvider extends SqlProvider {
    public String selectByUserUidAndCurrency();
}

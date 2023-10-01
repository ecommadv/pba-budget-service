package com.PBA.budgetservice.persistance.repository.sql;

public interface ExpenseSqlProvider extends SqlProvider {
    public String selectByUid();
    public String selectByUserUid();
    public String selectByFilters();
}

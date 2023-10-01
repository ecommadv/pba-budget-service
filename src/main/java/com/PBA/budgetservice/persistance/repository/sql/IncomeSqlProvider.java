package com.PBA.budgetservice.persistance.repository.sql;

public interface IncomeSqlProvider extends SqlProvider {
    public String selectByUid();
    public String selectByUserUid();
    public String selectByFilters();
    public String selectByRepetition();
}

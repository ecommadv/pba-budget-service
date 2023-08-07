package com.PBA.budgetservice.persistance.repository.sql;

public interface SqlProvider {
    public String insert();
    public String selectById();
    public String selectAll();
    public String deleteById();
    public String update();
}

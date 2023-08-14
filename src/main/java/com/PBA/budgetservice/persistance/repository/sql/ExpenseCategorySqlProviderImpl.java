package com.PBA.budgetservice.persistance.repository.sql;

import org.springframework.stereotype.Component;

@Component
public class ExpenseCategorySqlProviderImpl implements ExpenseCategorySqlProvider {
    @Override
    public String insert() {
        return """
                INSERT INTO expense_category (
                    id,
                    name,
                    uid
                ) VALUES (DEFAULT, ?, ?);
                """;
    }

    @Override
    public String selectById() {
        return """
                SELECT id, name, uid
                FROM expense_category
                WHERE
                    id = ?
                """;
    }

    @Override
    public String selectAll() {
        return """
               SELECT * FROM expense_category
               """;
    }

    @Override
    public String deleteById() {
        return """
                DELETE FROM expense_category
                WHERE id = ?
               """;
    }

    @Override
    public String update() {
        return """
                UPDATE expense_category
                SET name = ?, uid = ?
                WHERE id = ?
               """;
    }

    @Override
    public String selectByUid() {
        return """
                SELECT id, name, uid
                FROM expense_category
                WHERE
                    uid = ?
                """;
    }
}

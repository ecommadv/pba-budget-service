package com.PBA.budgetservice.persistance.repository.sql;

import org.springframework.stereotype.Component;

@Component
public class ExpenseSqlProviderImpl implements ExpenseSqlProvider {
    @Override
    public String insert() {
        return """
                INSERT INTO expense (
                    id,
                    amount,
                    name,
                    description,
                    currency,
                    uid,
                    account_id,
                    category_id,
                    created_at
                ) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?);
                """;
    }

    @Override
    public String selectById() {
        return """
                SELECT id, amount, name, description, currency, uid, account_id, category_id, created_at
                FROM expense
                WHERE
                    id = ?
                """;
    }

    @Override
    public String selectAll() {
        return """
               SELECT * FROM expense
               """;
    }

    @Override
    public String deleteById() {
        return """
                DELETE FROM expense
                WHERE id = ?
               """;
    }

    @Override
    public String update() {
        return """
                UPDATE expense
                SET amount = ?, name = ?, description = ?, currency = ?, uid = ?, account_id = ?, category_id = ?, created_at = ?
                WHERE id = ?
               """;
    }

    @Override
    public String selectByUid() {
        return """
                SELECT id, amount, name, description, currency, uid, account_id, category_id, created_at
                FROM expense
                WHERE
                    uid = ?
                """;
    }
}

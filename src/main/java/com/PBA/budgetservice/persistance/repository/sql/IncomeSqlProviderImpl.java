package com.PBA.budgetservice.persistance.repository.sql;


import org.springframework.stereotype.Component;

@Component
public class IncomeSqlProviderImpl implements IncomeSqlProvider {
    @Override
    public String insert() {
        return """
                INSERT INTO income (
                    id,
                    amount,
                    description,
                    currency,
                    uid,
                    account_id,
                    category_id,
                    created_at
                ) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?);
                """;
    }

    @Override
    public String selectById() {
        return """
                SELECT id, amount, description, currency, uid, account_id, category_id, created_at
                FROM income
                WHERE
                    id = ?
                """;
    }

    @Override
    public String selectAll() {
        return """
               SELECT * FROM income
               """;
    }

    @Override
    public String deleteById() {
        return """
                DELETE FROM income
                WHERE id = ?
               """;
    }

    @Override
    public String update() {
        return """
                UPDATE income
                SET amount = ?, description = ?, currency = ?, uid = ?, account_id = ?, category_id = ?, created_at = ?
                WHERE id = ?
               """;
    }

    @Override
    public String selectByUid() {
        return """
                SELECT id, amount, description, currency, uid, account_id, category_id, created_at
                FROM income
                WHERE
                    uid = ?
                """;
    }
}

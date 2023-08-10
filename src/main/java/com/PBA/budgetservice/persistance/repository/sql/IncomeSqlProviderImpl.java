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
                    account_id,
                    category_id
                ) VALUES (DEFAULT, ?, ?, ?, ?, ?);
                """;
    }

    @Override
    public String selectById() {
        return """
                SELECT id, amount, description, currency, account_id, category_id
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
                SET amount = ?, description = ?, currency = ?, account_id = ?, category_id = ?
                WHERE id = ?
               """;
    }
}

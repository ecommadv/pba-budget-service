package com.PBA.budgetservice.persistance.repository.sql;

import org.springframework.stereotype.Component;

@Component
public class AccountSqlProviderImpl implements AccountSqlProvider {
    @Override
    public String insert() {
        return """
                INSERT INTO account (
                    id,
                    user_uid,
                    currency
                ) VALUES (DEFAULT, ?, ?);
                """;
    }

    @Override
    public String selectById() {
        return """
                SELECT id, user_uid, currency
                FROM account
                WHERE
                    id = ?
                """;
    }

    @Override
    public String selectAll() {
        return """
               SELECT * FROM account
               """;
    }

    @Override
    public String deleteById() {
        return """
                DELETE FROM account
                WHERE id = ?
               """;
    }

    @Override
    public String update() {
        return """
                UPDATE account
                SET user_uid = ?, currency = ?
                WHERE id = ?
               """;
    }

    @Override
    public String selectByUserUidAndCurrency() {
        return """
                SELECT id, user_uid, currency
                FROM account
                WHERE
                    user_uid = ? AND currency = ?
                """;
    }
}

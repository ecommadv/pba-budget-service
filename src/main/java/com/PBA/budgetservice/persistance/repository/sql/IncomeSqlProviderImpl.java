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
                    created_at,
                    repetition
                ) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?);
                """;
    }

    @Override
    public String selectById() {
        return """
                SELECT id, amount, description, currency, uid, account_id, category_id, created_at, repetition
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
                SET amount = ?, description = ?, currency = ?, uid = ?, account_id = ?, category_id = ?, created_at = ?, repetition = ?
                WHERE id = ?
               """;
    }

    @Override
    public String selectByUid() {
        return """
                SELECT id, amount, description, currency, uid, account_id, category_id, created_at, repetition
                FROM income
                WHERE
                    uid = ?
                """;
    }

    @Override
    public String selectByUserUid() {
        return """
                SELECT income.*
                FROM income
                    INNER JOIN account ON income.account_id = account.id
                WHERE user_uid = ?
                """;
    }

    @Override
    public String selectByFilters() {
        return """
               SELECT income.*
               FROM income
                    INNER JOIN account ON income.account_id = account.id
                    INNER JOIN income_category ON income.category_id = income_category.id
               WHERE
                    (:userUid is null OR user_uid = :userUid)
                    AND
                    (:categoryName is null OR income_category.name = :categoryName)
                    AND
                    (:currency is null OR income.currency = :currency)
                    AND
                    (:dateAfter is null OR income.created_at >= :dateAfter)
                    AND
                    (:dateBefore is null OR income.created_at <= :dateBefore)
               """;
    }

    @Override
    public String selectByRepetition() {
        return """
               SELECT *
               FROM income
               WHERE repetition = ?
               """;
    }
}

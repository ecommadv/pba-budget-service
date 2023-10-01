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
                    created_at,
                    repetition
                ) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?);
                """;
    }

    @Override
    public String selectById() {
        return """
                SELECT id, amount, name, description, currency, uid, account_id, category_id, created_at, repetition
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
                SET amount = ?, name = ?, description = ?, currency = ?, uid = ?, account_id = ?, category_id = ?, created_at = ?, repetition = ?
                WHERE id = ?
               """;
    }

    @Override
    public String selectByUid() {
        return """
                SELECT id, amount, name, description, currency, uid, account_id, category_id, created_at, repetition
                FROM expense
                WHERE
                    uid = ?
                """;
    }

    @Override
    public String selectByUserUid() {
        return """
                SELECT expense.*
                FROM expense
                    INNER JOIN account ON expense.account_id = account.id
                WHERE user_uid = ?
                """;
    }

    @Override
    public String selectByFilters() {
        return """
               SELECT expense.*
               FROM expense
                    INNER JOIN account ON expense.account_id = account.id
                    INNER JOIN expense_category ON expense.category_id = expense_category.id
               WHERE
                    (:userUid is null OR user_uid = :userUid)
                    AND
                    (:expenseName is null OR expense.name = :expenseName)
                    AND
                    (:categoryName is null OR expense_category.name = :categoryName)
                    AND
                    (:currency is null OR expense.currency = :currency)
                    AND
                    (:dateAfter is null OR expense.created_at >= :dateAfter)
                    AND
                    (:dateBefore is null OR expense.created_at <= :dateBefore)
               """;
    }

    @Override
    public String selectByRepetition() {
        return """
               SELECT *
               FROM expense
               WHERE repetition = ?
               """;
    }
}

package com.PBA.budgetservice.persistance.repository.sql;

import org.springframework.stereotype.Component;

@Component
public class IncomeCategorySqlProviderImpl implements IncomeCategorySqlProvider {
    @Override
    public String insert() {
        return """
                INSERT INTO income_category (
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
                FROM income_category
                WHERE
                    id = ?
                """;
    }

    @Override
    public String selectAll() {
        return """
               SELECT * FROM income_category
               """;
    }

    @Override
    public String deleteById() {
        return """
                DELETE FROM income_category
                WHERE id = ?
               """;
    }

    @Override
    public String update() {
        return """
                UPDATE income_category
                SET name = ?, uid = ?
                WHERE id = ?
               """;
    }

    @Override
    public String selectByUid() {
        return """
                SELECT id, name, uid
                FROM income_category
                WHERE
                    uid = ?
                """;
    }
}

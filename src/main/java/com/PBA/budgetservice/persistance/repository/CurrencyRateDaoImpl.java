package com.PBA.budgetservice.persistance.repository;

import com.PBA.budgetservice.exceptions.BudgetDaoException;
import com.PBA.budgetservice.exceptions.ErrorCodes;
import com.PBA.budgetservice.persistance.model.CurrencyRate;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CurrencyRateDaoImpl implements CurrencyRateDao {
    private final MongoTemplate mongoTemplate;

    public CurrencyRateDaoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public CurrencyRate save(CurrencyRate currencyRate) {
        return mongoTemplate.insert(currencyRate);
    }

    @Override
    public Optional<CurrencyRate> getById(String id) {
        CurrencyRate currencyRate = mongoTemplate.findById(id, CurrencyRate.class);
        return Optional.ofNullable(currencyRate);
    }

    @Override
    public List<CurrencyRate> getAll() {
        return mongoTemplate.findAll(CurrencyRate.class);
    }

    @Override
    public long deleteById(String id) {
        Query query = new Query()
                .addCriteria(Criteria.where("_id").is(id));
        DeleteResult deleteResult = mongoTemplate.remove(query, CurrencyRate.class);

        long deletedCount = deleteResult.getDeletedCount();
        if (deletedCount == 0) {
            this.throwNotFoundException(id);
        }
        return deletedCount;
    }

    @Override
    public long update(CurrencyRate currencyRate, String id) {
        Query query = new Query()
                .addCriteria(Criteria.where("_id").is(id));
        UpdateResult updateResult = mongoTemplate.updateFirst(
                query,
                new Update()
                        .set("code", currencyRate.getCode())
                        .set("main_value", currencyRate.getMainValue()),
                CurrencyRate.class
        );

        long matchedCount = updateResult.getMatchedCount();
        if (matchedCount == 0) {
            this.throwNotFoundException(id);
        }
        return matchedCount;
    }

    private void throwNotFoundException(String id) {
        throw new BudgetDaoException(
                String.format("Currency rate with id %s does not exist", id),
                ErrorCodes.CURRENCY_RATE_NOT_FOUND,
                HttpStatus.NOT_FOUND
        );
    }
}

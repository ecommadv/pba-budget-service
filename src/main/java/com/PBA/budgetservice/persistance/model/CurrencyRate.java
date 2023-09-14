package com.PBA.budgetservice.persistance.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "currency_rate")
public class CurrencyRate {
    @Id
    private String id;
    private String code;
    @Field("main_value")
    private BigDecimal mainValue;
}

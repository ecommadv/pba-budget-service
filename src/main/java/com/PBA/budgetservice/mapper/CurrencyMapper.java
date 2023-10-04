package com.PBA.budgetservice.mapper;

import com.PBA.budgetservice.controller.request.CurrencyConversionRequest;
import com.PBA.budgetservice.controller.response.CurrencyConversionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper
public interface CurrencyMapper {
    @Mapping(target = "from", expression = "java(request.getFrom())")
    @Mapping(target = "to", expression = "java(request.getTo())")
    @Mapping(target = "convertedValue", expression = "java(convertedValue)")
    public CurrencyConversionResponse toConversionResponse(CurrencyConversionRequest request, BigDecimal convertedValue);
}

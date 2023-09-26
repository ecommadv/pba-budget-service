package com.pba.budgetservice.mockgenerators;

import com.PBA.budgetservice.persistance.model.IncomeCategory;

import java.util.Random;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IncomeCategoryMockGenerator {
    public static IncomeCategory generateMockIncomeCategory() {
        return IncomeCategory.builder()
                .id(new Random().nextLong())
                .name(UUID.randomUUID().toString())
                .uid(UUID.randomUUID())
                .build();
    }

    public static List<IncomeCategory> generateMockListOfIncomeCategories(int size) {
        return Stream.generate(IncomeCategoryMockGenerator::generateMockIncomeCategory)
                .limit(size)
                .collect(Collectors.toList());
    }
}

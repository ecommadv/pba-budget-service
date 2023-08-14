package mockgenerators;

import com.PBA.budgetservice.persistance.model.ExpenseCategory;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExpenseCategoryMockGenerator {
    public static ExpenseCategory generateMockExpenseCategory() {
        return ExpenseCategory.builder()
                .id(new Random().nextLong())
                .uid(UUID.randomUUID())
                .name(UUID.randomUUID().toString())
                .build();
    }

    public static List<ExpenseCategory> generateMockListOfExpenseCategories(int size) {
        return Stream.generate(ExpenseCategoryMockGenerator::generateMockExpenseCategory)
                .limit(size)
                .collect(Collectors.toList());
    }
}

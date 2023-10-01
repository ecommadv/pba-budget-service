package com.PBA.budgetservice.scheduler;

import com.PBA.budgetservice.mapper.ExpenseMapper;
import com.PBA.budgetservice.persistance.model.Expense;
import com.PBA.budgetservice.persistance.model.Repetition;
import com.PBA.budgetservice.service.ExpenseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Function;

@Service
public class ExpenseRepetitionSchedulerImpl implements ExpenseRepetitionScheduler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExpenseRepetitionSchedulerImpl.class);
    private final ExpenseService expenseService;
    private final ExpenseMapper expenseMapper;

    public ExpenseRepetitionSchedulerImpl(ExpenseService expenseService, ExpenseMapper expenseMapper) {
        this.expenseService = expenseService;
        this.expenseMapper = expenseMapper;
    }

    @Override
    @Scheduled(cron = "${scheduler.repetition_cron}")
    @Transactional
    public void addAllScheduledExpenses() {
        LOGGER.info("Expense repetition scheduler triggered at {}", LocalDateTime.now());

        this.addAllScheduledExpenses(Repetition.DAILY, this::dailyExpenseShouldBeAdded);
        this.addAllScheduledExpenses(Repetition.MONTHLY, this::monthlyExpenseShouldBeAdded);
    }

    private void addAllScheduledExpenses(Repetition repetition, Function<Expense, Boolean> expenseShouldBeAdded) {
        List<Expense> expenses = expenseService.getByRepetition(repetition);
        List<Expense> shouldBeAddedToday = expenses
                .stream()
                .filter(expenseShouldBeAdded::apply)
                .toList();
        shouldBeAddedToday.forEach(expense -> {
            Expense scheduledExpense = expenseMapper.toScheduledExpense(expense);
            expenseService.addExpense(scheduledExpense);
            LOGGER.info("Duplicated expense with uid {}. Repetition: {}", expense.getUid(), expense.getRepetition());
        });
    }

    private boolean monthlyExpenseShouldBeAdded(Expense expense) {
        return expense.getCreatedAtFloor().until(LocalDateTime.now(), ChronoUnit.MONTHS) >= 1
                && expense.getCreatedAt().getDayOfMonth() == LocalDateTime.now().getDayOfMonth();
    }

    private boolean dailyExpenseShouldBeAdded(Expense expense) {
        return expense.getCreatedAtFloor().until(LocalDateTime.now(), ChronoUnit.DAYS) >= 1;
    }
}

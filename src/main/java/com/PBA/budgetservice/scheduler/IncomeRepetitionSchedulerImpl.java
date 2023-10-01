package com.PBA.budgetservice.scheduler;

import com.PBA.budgetservice.mapper.IncomeMapper;
import com.PBA.budgetservice.persistance.model.Income;
import com.PBA.budgetservice.persistance.model.Repetition;
import com.PBA.budgetservice.service.IncomeService;
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
public class IncomeRepetitionSchedulerImpl implements IncomeRepetitionScheduler {
    private static final Logger LOGGER = LoggerFactory.getLogger(IncomeRepetitionSchedulerImpl.class);
    private final IncomeService incomeService;
    private final IncomeMapper incomeMapper;

    public IncomeRepetitionSchedulerImpl(IncomeService incomeService, IncomeMapper incomeMapper) {
        this.incomeService = incomeService;
        this.incomeMapper = incomeMapper;
    }

    @Override
    @Scheduled(cron = "${scheduler.repetition_cron}")
    @Transactional
    public void addAllScheduledIncomes() {
        LOGGER.info("Income repetition scheduler triggered at {}", LocalDateTime.now());

        this.addAllScheduledIncomes(Repetition.DAILY, this::dailyIncomeShouldBeAdded);
        this.addAllScheduledIncomes(Repetition.MONTHLY, this::monthlyIncomeShouldBeAdded);
    }

    private void addAllScheduledIncomes(Repetition repetition, Function<Income, Boolean> incomeShouldBeAdded) {
        List<Income> monthlyExpenses = incomeService.getByRepetition(repetition);
        List<Income> scheduledToday = monthlyExpenses
                .stream()
                .filter(incomeShouldBeAdded::apply)
                .toList();
        scheduledToday.forEach(income -> {
            Income scheduledIncome = incomeMapper.toScheduledIncome(income);
            incomeService.addIncome(scheduledIncome);
            LOGGER.info("Duplicated income with uid {} added. Repetition: {}", income.getUid(), income.getRepetition().name());
        });
    }

    private boolean monthlyIncomeShouldBeAdded(Income income) {
        return income.getCreatedAtFloor().until(LocalDateTime.now(), ChronoUnit.MONTHS) >= 1
                && income.getCreatedAt().getDayOfMonth() == LocalDateTime.now().getDayOfMonth();
    }

    private boolean dailyIncomeShouldBeAdded(Income income) {
        return income.getCreatedAtFloor().until(LocalDateTime.now(), ChronoUnit.DAYS) >= 1;
    }
}

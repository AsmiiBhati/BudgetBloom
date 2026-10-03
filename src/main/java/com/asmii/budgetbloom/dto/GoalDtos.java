package com.asmii.budgetbloom.dto;

import com.asmii.budgetbloom.entity.SavingsGoal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.UUID;

public final class GoalDtos {
    private GoalDtos() {}

    public record Request(
            @NotNull UUID userId,
            @NotBlank @Size(max = 100) String name,
            @NotNull @DecimalMin("0.01") @Digits(integer = 13, fraction = 2) BigDecimal targetAmount, LocalDate targetDate) {}

    public record ContributionRequest(@NotNull @DecimalMin("0.01") @Digits(integer = 13, fraction = 2) BigDecimal amount) {}

    public record Response(UUID id, UUID userId, String name, BigDecimal targetAmount, BigDecimal currentAmount, LocalDate targetDate, BigDecimal percentComplete, boolean completed) {
        public static Response from(SavingsGoal g) {
            BigDecimal hundred = BigDecimal.valueOf(100);
            BigDecimal pct = g.getCurrentAmount().multiply(hundred).divide(g.getTargetAmount(), 2, RoundingMode.HALF_UP).min(hundred);
            return new Response(g.getId(), g.getUserId(), g.getName(), g.getTargetAmount(), g.getCurrentAmount(), g.getTargetDate(), pct, g.getCurrentAmount().compareTo(g.getTargetAmount()) >= 0);
        }
    }
}

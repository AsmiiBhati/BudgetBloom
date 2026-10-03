package com.asmii.budgetbloom.dto;

import com.asmii.budgetbloom.entity.Budget;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

public final class BudgetDtos {
    private BudgetDtos() {}

    public record Request(
            @NotNull UUID userId,
            @NotNull UUID categoryId,
            @NotNull @DecimalMin("0.01") @Digits(integer = 13, fraction = 2) BigDecimal amount,
            @NotBlank @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])$", message = "must be in yyyy-MM format") String month) {}

    public record Response(UUID id, UUID userId, UUID categoryId, String categoryName, BigDecimal amount, String month) {
        public static Response from(Budget b) {
            return new Response(b.getId(), b.getUserId(), b.getCategory().getId(), b.getCategory().getName(), b.getAmount(), YearMonth.from(b.getMonthStart()).toString());
        }
    }

    /** variance = budgeted - actual (negative means overspent). */
    public record VarianceItem(UUID categoryId, String categoryName, BigDecimal budgeted, BigDecimal actual, BigDecimal variance, BigDecimal percentUsed, boolean overBudget) {}

    public record VarianceReport(String month, List<VarianceItem> items, BigDecimal totalBudgeted, BigDecimal totalActual, BigDecimal totalVariance) {}
}

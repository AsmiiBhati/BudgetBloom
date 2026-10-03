package com.asmii.budgetbloom.dto;

import com.asmii.budgetbloom.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public final class ReportDtos {
    private ReportDtos() {}

    public record CategorySpend(UUID categoryId, String categoryName, BigDecimal total, BigDecimal percentOfTotal) {}

    public record SpendingReport(LocalDate from, LocalDate to, TransactionType type, BigDecimal grandTotal, List<CategorySpend> categories) {}
}

package com.asmii.budgetbloom.controller;

import com.asmii.budgetbloom.dto.ReportDtos.SpendingReport;
import com.asmii.budgetbloom.enums.TransactionType;
import com.asmii.budgetbloom.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
    private final TransactionService transactions;

    // Category-based aggregation: /api/reports/spending-by-category?userId=..&from=2026-10-01&to=2026-10-31
    @GetMapping("/spending-by-category")
    public SpendingReport spendingByCategory(
            @RequestParam UUID userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(defaultValue = "EXPENSE") TransactionType type) {
        return transactions.spendingByCategory(userId, type, from, to);
    }
}


package com.asmii.budgetbloom.dto;

import com.asmii.budgetbloom.entity.FinancialTransaction;
import com.asmii.budgetbloom.enums.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public final class TransactionDtos {
    private TransactionDtos() {}

    public record Request(
            @NotNull UUID userId,
            @NotNull UUID categoryId,
            @NotNull @DecimalMin("0.01") @Digits(integer = 13, fraction = 2) BigDecimal amount,
            @NotNull TransactionType type,
            @Size(max = 255) String description,
            @NotNull LocalDate transactionDate) {}

    public record Response(UUID id, UUID userId, UUID categoryId, String categoryName, BigDecimal amount, TransactionType type, String description, LocalDate transactionDate) {
        public static Response from(FinancialTransaction t) {
            return new Response(t.getId(), t.getUserId(), t.getCategory().getId(), t.getCategory().getName(), t.getAmount(), t.getType(), t.getDescription(), t.getTransactionDate());
        }
    }
}

package com.asmii.budgetbloom.dto;

import com.asmii.budgetbloom.entity.Category;
import com.asmii.budgetbloom.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public final class CategoryDtos {
    private CategoryDtos() {}

    public record Request(@NotNull UUID userId, @NotBlank @Size(max = 100) String name, @NotNull TransactionType type) {}

    public record Response(UUID id, UUID userId, String name, TransactionType type) {
        public static Response from(Category c) {
            return new Response(c.getId(), c.getUserId(), c.getName(), c.getType());
        }
    }
}

package com.asmii.budgetbloom.dto;

import com.asmii.budgetbloom.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public final class UserDtos {
    private UserDtos() {}

    public record CreateRequest(@NotBlank @Size(max = 100) String name, @NotBlank @Email @Size(max = 255) String email, @NotBlank @Size(min = 8, max = 72) String password) {}

    public record Response(UUID id, String name, String email, LocalDateTime createdAt) {
        public static Response from(User u) {
            return new Response(u.getId(), u.getName(), u.getEmail(), u.getCreatedAt());
        }
    }
}

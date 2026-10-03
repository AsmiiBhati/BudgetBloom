package com.asmii.budgetbloom.repository;

import com.asmii.budgetbloom.entity.SavingsGoal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SavingsGoalRepository extends JpaRepository<SavingsGoal, UUID> {
    List<SavingsGoal> findByUserIdOrderByCreatedAtDesc(UUID userId);
}

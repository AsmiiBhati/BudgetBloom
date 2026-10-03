package com.asmii.budgetbloom.repository;

import com.asmii.budgetbloom.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findByUserIdOrderByNameAsc(UUID userId);
    boolean existsByUserIdAndNameIgnoreCase(UUID userId, String name);
}

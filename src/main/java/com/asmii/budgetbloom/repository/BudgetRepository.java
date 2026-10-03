package com.asmii.budgetbloom.repository;

import com.asmii.budgetbloom.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface BudgetRepository extends JpaRepository<Budget, UUID> {

    boolean existsByUserIdAndCategoryIdAndMonthStart(UUID userId, UUID categoryId, LocalDate monthStart);

    /** JOIN FETCH loads each budget's category in the same query (avoids N+1 when we read category names). */
    @Query("""
            SELECT b FROM Budget b JOIN FETCH b.category c
            WHERE b.userId = :userId AND b.monthStart = :monthStart
            ORDER BY c.name
            """)
    List<Budget> findForMonthWithCategory(@Param("userId") UUID userId, @Param("monthStart") LocalDate monthStart);
}

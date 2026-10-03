package com.asmii.budgetbloom.repository;

import com.asmii.budgetbloom.entity.FinancialTransaction;
import com.asmii.budgetbloom.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<FinancialTransaction, UUID>, JpaSpecificationExecutor<FinancialTransaction> {
    // Category is fetched in the same query, so listing a page never triggers one extra select per row
    @Override
    @EntityGraph(attributePaths = "category")
    Page<FinancialTransaction> findAll(Specification<FinancialTransaction> spec, Pageable pageable);

    /** Category-based aggregation: total per category in a date range, biggest first. */
    @Query("""
            SELECT c.id AS categoryId, c.name AS categoryName, SUM(t.amount) AS total
            FROM FinancialTransaction t JOIN t.category c
            WHERE t.userId = :userId AND t.type = :type
              AND t.transactionDate BETWEEN :startDate AND :endDate
            GROUP BY c.id, c.name
            ORDER BY SUM(t.amount) DESC
            """)
    List<CategoryTotal> totalsByCategory(@Param("userId") UUID userId, @Param("type") TransactionType type, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    interface CategoryTotal {
        UUID getCategoryId();
        String getCategoryName();
        BigDecimal getTotal();
    }
}

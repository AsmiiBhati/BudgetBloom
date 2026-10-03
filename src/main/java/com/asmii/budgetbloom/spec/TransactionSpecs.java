package com.asmii.budgetbloom.spec;

import com.asmii.budgetbloom.entity.FinancialTransaction;
import com.asmii.budgetbloom.enums.TransactionType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TransactionSpecs {
    private TransactionSpecs() {}

    public static Specification<FinancialTransaction> filter(UUID userId, TransactionType type, UUID categoryId, LocalDate from, LocalDate to) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("userId"), userId));
            if (type != null)
            {
                predicates.add(cb.equal(root.get("type"), type));
            }
            if (categoryId != null)
            {
                predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            }
            if (from != null)
            {
                predicates.add(cb.greaterThanOrEqualTo(root.<LocalDate>get("transactionDate"), from));
            }
            if (to != null)
            {
                predicates.add(cb.lessThanOrEqualTo(root.<LocalDate>get("transactionDate"), to));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

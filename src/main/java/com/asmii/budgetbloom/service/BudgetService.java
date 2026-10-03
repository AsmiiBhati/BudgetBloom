package com.asmii.budgetbloom.service;

import com.asmii.budgetbloom.dto.BudgetDtos.Request;
import com.asmii.budgetbloom.dto.BudgetDtos.Response;
import com.asmii.budgetbloom.dto.BudgetDtos.VarianceItem;
import com.asmii.budgetbloom.dto.BudgetDtos.VarianceReport;
import com.asmii.budgetbloom.entity.Budget;
import com.asmii.budgetbloom.entity.Category;
import com.asmii.budgetbloom.enums.TransactionType;
import com.asmii.budgetbloom.exception.BadRequestException;
import com.asmii.budgetbloom.exception.ConflictException;
import com.asmii.budgetbloom.exception.ResourceNotFoundException;
import com.asmii.budgetbloom.repository.BudgetRepository;
import com.asmii.budgetbloom.repository.CategoryRepository;
import com.asmii.budgetbloom.repository.TransactionRepository;
import com.asmii.budgetbloom.repository.TransactionRepository.CategoryTotal;
import com.asmii.budgetbloom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository budgets;
    private final CategoryRepository categories;
    private final TransactionRepository transactions;
    private final UserRepository users;

    @Transactional
    public Response create(Request req) {
        if (!users.existsById(req.userId())) {
            throw new ResourceNotFoundException("User not found: " + req.userId());
        }
        Category category = categories.findById(req.categoryId()).filter(c -> c.getUserId().equals(req.userId())).orElseThrow(() -> new ResourceNotFoundException("Category not found: " + req.categoryId()));
        if (category.getType() != TransactionType.EXPENSE) {
            throw new BadRequestException("Budgets can only be set on EXPENSE categories");
        }
        YearMonth month = parseMonth(req.month());
        if (budgets.existsByUserIdAndCategoryIdAndMonthStart(req.userId(), category.getId(), month.atDay(1))) {
            throw new ConflictException("A budget for this category and month already exists");
        }
        Budget b = new Budget();
        b.setUserId(req.userId());
        b.setCategory(category);
        b.setAmount(req.amount());
        b.setMonthStart(month.atDay(1));
        return Response.from(budgets.save(b));
    }

    @Transactional(readOnly = true)
    public List<Response> list(UUID userId, String month) {
        return budgets.findForMonthWithCategory(userId, parseMonth(month).atDay(1)).stream().map(Response::from).toList();
    }

    @Transactional
    public void delete(UUID id) {
        if (!budgets.existsById(id)) {
            throw new ResourceNotFoundException("Budget not found: " + id);
        }
        budgets.deleteById(id);
    }
    @Transactional(readOnly = true)
    public VarianceReport variance(UUID userId, String month) {
        YearMonth ym = parseMonth(month);
        List<Budget> monthBudgets = budgets.findForMonthWithCategory(userId, ym.atDay(1));

        Map<UUID, BigDecimal> actualByCategory = new HashMap<>();
        for (CategoryTotal row : transactions.totalsByCategory(userId, TransactionType.EXPENSE, ym.atDay(1), ym.atEndOfMonth())) {
            actualByCategory.put(row.getCategoryId(), row.getTotal());
        }

        BigDecimal hundred = BigDecimal.valueOf(100);
        BigDecimal totalBudgeted = BigDecimal.ZERO;
        BigDecimal totalActual = BigDecimal.ZERO;
        List<VarianceItem> items = new java.util.ArrayList<>();

        for (Budget b : monthBudgets) {
            BigDecimal budgeted = b.getAmount();
            BigDecimal actual = actualByCategory.getOrDefault(b.getCategory().getId(), BigDecimal.ZERO);
            BigDecimal percentUsed = actual.multiply(hundred).divide(budgeted, 2, RoundingMode.HALF_UP);
            items.add(new VarianceItem(b.getCategory().getId(), b.getCategory().getName(), budgeted, actual, budgeted.subtract(actual), percentUsed, actual.compareTo(budgeted) > 0));
            totalBudgeted = totalBudgeted.add(budgeted);
            totalActual = totalActual.add(actual);
        }
        return new VarianceReport(ym.toString(), items, totalBudgeted, totalActual, totalBudgeted.subtract(totalActual));
    }

    private YearMonth parseMonth(String month) {
        try {
            return YearMonth.parse(month);
        } catch (DateTimeParseException | NullPointerException e) {
            throw new BadRequestException("month must be in yyyy-MM format, e.g. 2026-10");
        }
    }
}

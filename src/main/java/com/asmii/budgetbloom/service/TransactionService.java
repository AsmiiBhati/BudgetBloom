package com.asmii.budgetbloom.service;

import com.asmii.budgetbloom.dto.PageResponse;
import com.asmii.budgetbloom.dto.ReportDtos.CategorySpend;
import com.asmii.budgetbloom.dto.ReportDtos.SpendingReport;
import com.asmii.budgetbloom.dto.TransactionDtos.Request;
import com.asmii.budgetbloom.dto.TransactionDtos.Response;
import com.asmii.budgetbloom.entity.Category;
import com.asmii.budgetbloom.entity.FinancialTransaction;
import com.asmii.budgetbloom.enums.TransactionType;
import com.asmii.budgetbloom.exception.BadRequestException;
import com.asmii.budgetbloom.exception.ResourceNotFoundException;
import com.asmii.budgetbloom.repository.CategoryRepository;
import com.asmii.budgetbloom.repository.TransactionRepository;
import com.asmii.budgetbloom.repository.UserRepository;
import com.asmii.budgetbloom.spec.TransactionSpecs;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactions;
    private final CategoryRepository categories;
    private final UserRepository users;

    @Transactional
    public Response create(Request req) {
        if (!users.existsById(req.userId())) {
            throw new ResourceNotFoundException("User not found: " + req.userId());
        }
        Category category = categories.findById(req.categoryId()).filter(c -> c.getUserId().equals(req.userId())).orElseThrow(() -> new ResourceNotFoundException("Category not found: " + req.categoryId()));
        if (category.getType() != req.type()) {
            throw new BadRequestException("Transaction type must match the category type (" + category.getType() + ")");
        }
        FinancialTransaction t = new FinancialTransaction();
        t.setUserId(req.userId());
        t.setCategory(category);
        t.setAmount(req.amount());
        t.setType(req.type());
        t.setDescription(req.description());
        t.setTransactionDate(req.transactionDate());
        return Response.from(transactions.save(t));
    }

    @Transactional(readOnly = true)
    public PageResponse<Response> search(UUID userId, TransactionType type, UUID categoryId, LocalDate from, LocalDate to, Pageable pageable) {
        if (from != null && to != null && from.isAfter(to)) {
            throw new BadRequestException("'from' must not be after 'to'");
        }
        Page<FinancialTransaction> page = transactions.findAll(TransactionSpecs.filter(userId, type, categoryId, from, to), pageable);
        return PageResponse.from(page.map(Response::from));
    }

    @Transactional
    public void delete(UUID id) {
        if (!transactions.existsById(id)) {
            throw new ResourceNotFoundException("Transaction not found: " + id);
        }
        transactions.deleteById(id);
    }

    @Transactional(readOnly = true)
    public SpendingReport spendingByCategory(UUID userId, TransactionType type, LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new BadRequestException("'from' must not be after 'to'");
        }
        var rows = transactions.totalsByCategory(userId, type, from, to);
        BigDecimal grand = rows.stream().map(r -> r.getTotal()).reduce(BigDecimal.ZERO, BigDecimal::add);
        List<CategorySpend> items = rows.stream().map(r -> new CategorySpend(r.getCategoryId(), r.getCategoryName(), r.getTotal(), grand.signum() == 0 ? BigDecimal.ZERO : r.getTotal().multiply(BigDecimal.valueOf(100)).divide(grand, 2, RoundingMode.HALF_UP))).toList();
        return new SpendingReport(from, to, type, grand, items);
    }
}

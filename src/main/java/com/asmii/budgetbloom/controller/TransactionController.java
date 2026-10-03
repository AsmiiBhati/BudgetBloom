package com.asmii.budgetbloom.controller;

import com.asmii.budgetbloom.dto.PageResponse;
import com.asmii.budgetbloom.dto.TransactionDtos.Request;
import com.asmii.budgetbloom.dto.TransactionDtos.Response;
import com.asmii.budgetbloom.enums.TransactionType;
import com.asmii.budgetbloom.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Response create(@Valid @RequestBody Request req) {
        return service.create(req);
    }

    // e.g. /api/transactions?userId=..&type=EXPENSE&from=2026-10-01&to=2026-10-31&page=0&size=20
    @GetMapping
    public PageResponse<Response> search(
            @RequestParam UUID userId,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @PageableDefault(size = 20, sort = "transactionDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return service.search(userId, type, categoryId, from, to, pageable);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}

package com.asmii.budgetbloom.controller;

import com.asmii.budgetbloom.dto.BudgetDtos.Request;
import com.asmii.budgetbloom.dto.BudgetDtos.Response;
import com.asmii.budgetbloom.dto.BudgetDtos.VarianceReport;
import com.asmii.budgetbloom.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {
    private final BudgetService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Response create(@Valid @RequestBody Request req) {
        return service.create(req);
    }

    @GetMapping
    public List<Response> list(@RequestParam UUID userId, @RequestParam String month) {
        return service.list(userId, month);
    }

    // Monthly budget-vs-actual variance, e.g. /api/budgets/variance?userId=..&month=2026-10
    @GetMapping("/variance")
    public VarianceReport variance(@RequestParam UUID userId, @RequestParam String month) {
        return service.variance(userId, month);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}

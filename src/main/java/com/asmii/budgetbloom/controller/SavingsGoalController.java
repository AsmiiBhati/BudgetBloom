package com.asmii.budgetbloom.controller;

import com.asmii.budgetbloom.dto.GoalDtos.ContributionRequest;
import com.asmii.budgetbloom.dto.GoalDtos.Request;
import com.asmii.budgetbloom.dto.GoalDtos.Response;
import com.asmii.budgetbloom.service.SavingsGoalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/savings-goals")
@RequiredArgsConstructor
public class SavingsGoalController {
    private final SavingsGoalService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Response create(@Valid @RequestBody Request req) {
        return service.create(req);
    }

    @GetMapping
    public List<Response> list(@RequestParam UUID userId) {
        return service.list(userId);
    }

    @PatchMapping("/{id}/contribute")
    public Response contribute(@PathVariable UUID id, @Valid @RequestBody ContributionRequest req) {
        return service.contribute(id, req.amount());
    }
}

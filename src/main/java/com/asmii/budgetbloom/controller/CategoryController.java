package com.asmii.budgetbloom.controller;

import com.asmii.budgetbloom.dto.CategoryDtos.Request;
import com.asmii.budgetbloom.dto.CategoryDtos.Response;
import com.asmii.budgetbloom.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Response create(@Valid @RequestBody Request req) {
        return service.create(req);
    }

    @GetMapping
    public List<Response> list(@RequestParam UUID userId) {
        return service.list(userId);
    }
}

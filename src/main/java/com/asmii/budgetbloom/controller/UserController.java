package com.asmii.budgetbloom.controller;

import com.asmii.budgetbloom.dto.UserDtos.CreateRequest;
import com.asmii.budgetbloom.dto.UserDtos.Response;
import com.asmii.budgetbloom.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Response create(@Valid @RequestBody CreateRequest req) {
        return service.create(req);
    }

    @GetMapping("/{id}")
    public Response get(@PathVariable UUID id) {
        return service.get(id);
    }
}

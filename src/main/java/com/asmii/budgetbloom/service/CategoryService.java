package com.asmii.budgetbloom.service;

import com.asmii.budgetbloom.dto.CategoryDtos.Request;
import com.asmii.budgetbloom.dto.CategoryDtos.Response;
import com.asmii.budgetbloom.entity.Category;
import com.asmii.budgetbloom.exception.ConflictException;
import com.asmii.budgetbloom.exception.ResourceNotFoundException;
import com.asmii.budgetbloom.repository.CategoryRepository;
import com.asmii.budgetbloom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categories;
    private final UserRepository users;

    @Transactional
    public Response create(Request req) {
        if (!users.existsById(req.userId())) {
            throw new ResourceNotFoundException("User not found: " + req.userId());
        }
        String name = req.name().trim();
        if (categories.existsByUserIdAndNameIgnoreCase(req.userId(), name))
        {
            throw new ConflictException("Category already exists: " + name);
        }
        Category c = new Category();
        c.setUserId(req.userId());
        c.setName(name);
        c.setType(req.type());
        return Response.from(categories.save(c));
    }

    @Transactional(readOnly = true)
    public List<Response> list(UUID userId) {
        return categories.findByUserIdOrderByNameAsc(userId).stream().map(Response::from).toList();
    }
}

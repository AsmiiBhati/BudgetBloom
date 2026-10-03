package com.asmii.budgetbloom.service;

import com.asmii.budgetbloom.dto.UserDtos.CreateRequest;
import com.asmii.budgetbloom.dto.UserDtos.Response;
import com.asmii.budgetbloom.entity.User;
import com.asmii.budgetbloom.exception.ConflictException;
import com.asmii.budgetbloom.exception.ResourceNotFoundException;
import com.asmii.budgetbloom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository users;
    private final PasswordEncoder encoder;

    @Transactional
    public Response create(CreateRequest req) {
        String email = req.email().trim().toLowerCase();
        if (users.existsByEmail(email)) {
            throw new ConflictException("Email already registered");
        }
        User user = new User();
        user.setName(req.name().trim());
        user.setEmail(email);
        user.setPasswordHash(encoder.encode(req.password()));
        return Response.from(users.save(user));
    }

    @Transactional(readOnly = true)
    public Response get(UUID id) {
        return users.findById(id).map(Response::from)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
    }
}

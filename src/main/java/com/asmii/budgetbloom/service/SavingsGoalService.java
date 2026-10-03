package com.asmii.budgetbloom.service;

import com.asmii.budgetbloom.dto.GoalDtos.Request;
import com.asmii.budgetbloom.dto.GoalDtos.Response;
import com.asmii.budgetbloom.entity.SavingsGoal;
import com.asmii.budgetbloom.exception.ResourceNotFoundException;
import com.asmii.budgetbloom.repository.SavingsGoalRepository;
import com.asmii.budgetbloom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SavingsGoalService {
    private final SavingsGoalRepository goals;
    private final UserRepository users;

    @Transactional
    public Response create(Request req) {
        if (!users.existsById(req.userId())) {
            throw new ResourceNotFoundException("User not found: " + req.userId());
        }
        SavingsGoal g = new SavingsGoal();
        g.setUserId(req.userId());
        g.setName(req.name().trim());
        g.setTargetAmount(req.targetAmount());
        g.setCurrentAmount(BigDecimal.ZERO);
        g.setTargetDate(req.targetDate());
        return Response.from(goals.save(g));
    }

    @Transactional(readOnly = true)
    public List<Response> list(UUID userId) {
        return goals.findByUserIdOrderByCreatedAtDesc(userId).stream().map(Response::from).toList();
    }

    @Transactional
    public Response contribute(UUID id, BigDecimal amount) {
        SavingsGoal g = goals.findById(id).orElseThrow(() -> new ResourceNotFoundException("Savings goal not found: " + id));
        g.setCurrentAmount(g.getCurrentAmount().add(amount));
        return Response.from(goals.saveAndFlush(g));
    }

}

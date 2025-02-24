package com.example.petprojectbudgettracker.services;

import com.example.petprojectbudgettracker.models.Goal;
import com.example.petprojectbudgettracker.models.User;
import com.example.petprojectbudgettracker.repositories.GoalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoalService {
    private final GoalRepository goalRepository;
    private final AuthService authService;

    public GoalService(GoalRepository goalRepository, AuthService authService) {
        this.goalRepository = goalRepository;
        this.authService = authService;
    }

    public List<Goal> getGoalsByUser() {
        User user = authService.getCurrentUser();
        return goalRepository.findByUser_Id(user.getId());
    }

    public Goal createGoal(String name, double targetAmount) {
        User user = authService.getCurrentUser();

        Goal goal = new Goal();
        goal.setUser(user);
        goal.setName(name);
        goal.setTargetAmount(targetAmount);
        return goalRepository.save(goal);
    }

    public Goal updateGoal(Long id, double amount) {
        Goal goal = goalRepository.findById(id).orElseThrow(() -> new RuntimeException("Goal not found"));
        goal.setCurrentAmount(goal.getCurrentAmount() + amount);
        return goalRepository.save(goal);
    }

    public void deleteGoal(Long id) {
        goalRepository.deleteById(id);
    }
}

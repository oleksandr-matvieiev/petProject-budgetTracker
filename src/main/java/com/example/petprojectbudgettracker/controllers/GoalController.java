package com.example.petprojectbudgettracker.controllers;

import com.example.petprojectbudgettracker.models.Goal;
import com.example.petprojectbudgettracker.services.GoalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
public class GoalController {
    private final GoalService goalService;

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @GetMapping("/get-goals")
    public ResponseEntity<List<Goal>> getUserGoals() {
        return ResponseEntity.ok(goalService.getGoalsByUser());
    }

    @PostMapping("/create")
    public ResponseEntity<Goal> createGoal(@RequestParam String name, @RequestParam double targetAmount) {
        return ResponseEntity.ok(goalService.createGoal(name, targetAmount));
    }

    @PostMapping("/update")
    public ResponseEntity<Goal> updateGoal(@RequestParam Long goalId, @RequestParam double amount) {
        return ResponseEntity.ok(goalService.updateGoal(goalId, amount));
    }

    @DeleteMapping("/{goalId}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long goalId) {
        goalService.deleteGoal(goalId);
        return ResponseEntity.noContent().build();
    }
}

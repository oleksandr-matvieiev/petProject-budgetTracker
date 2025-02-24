package com.example.petprojectbudgettracker.repositories;

import com.example.petprojectbudgettracker.models.Goal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findByUser_Id(long id);
}

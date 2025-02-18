package com.example.petprojectbudgettracker.repositories;

import com.example.petprojectbudgettracker.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}

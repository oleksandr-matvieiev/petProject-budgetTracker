package com.example.petprojectbudgettracker.repositories;

import com.example.petprojectbudgettracker.models.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
}

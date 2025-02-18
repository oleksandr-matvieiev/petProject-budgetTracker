package com.example.petprojectbudgettracker.repositories;

import com.example.petprojectbudgettracker.models.Category;
import com.example.petprojectbudgettracker.models.SubCategory;
import com.example.petprojectbudgettracker.models.TransactionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
    List<SubCategory> findAllByCategoryTransactionCategory(TransactionCategory category_transactionCategory);

}

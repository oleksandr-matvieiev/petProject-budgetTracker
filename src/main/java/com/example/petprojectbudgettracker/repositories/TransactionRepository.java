package com.example.petprojectbudgettracker.repositories;

import com.example.petprojectbudgettracker.models.Transaction;
import com.example.petprojectbudgettracker.models.TransactionCategory;
import com.example.petprojectbudgettracker.models.TransactionType;
import com.example.petprojectbudgettracker.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserId(Long user_id);

    List<Transaction> findByUserIdAndType(Long userId, TransactionType type);

    List<Transaction> findByUserIdAndCategory(Long userId, TransactionCategory category);


}

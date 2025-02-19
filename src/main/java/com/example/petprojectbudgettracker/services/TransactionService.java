package com.example.petprojectbudgettracker.services;

import com.example.petprojectbudgettracker.models.*;
import com.example.petprojectbudgettracker.repositories.SubCategoryRepository;
import com.example.petprojectbudgettracker.repositories.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AuthService authService;
    private final SubCategoryRepository subCategoryRepository;

    public TransactionService(TransactionRepository transactionRepository, AuthService authService, SubCategoryRepository subCategoryRepository) {
        this.transactionRepository = transactionRepository;
        this.authService = authService;
        this.subCategoryRepository = subCategoryRepository;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction getTransactionsById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    @Transactional
    public Transaction saveTransaction(double amount, TransactionType type,
                                       TransactionCategory category, Long subCategoryId,
                                       String description) {
        User user = authService.getCurrentUser();

        SubCategory subCategory = subCategoryRepository.findById(subCategoryId)
                .orElseThrow(() -> new RuntimeException("SubCategory not found"));

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setCategory(category);
        transaction.setSubCategory(subCategory);
        transaction.setTransactionDate(LocalDate.now());
        transaction.setDescription(description);

        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction updateTransaction(Long id, Double amount, TransactionType type,
                                         TransactionCategory category, Long subCategoryId,
                                         String description) {
        User user = authService.getCurrentUser();

        Transaction transaction = getTransactionsById(id);


        if (amount != null) transaction.setAmount(amount);
        if (type != null) transaction.setType(type);
        if (category != null) transaction.setCategory(category);
        if (subCategoryId != null) {
            SubCategory subCategory = subCategoryRepository.findById(subCategoryId)
                    .orElseThrow(() -> new RuntimeException("SubCategory not found"));
            transaction.setSubCategory(subCategory);
        }
        if (description!=null) transaction.setDescription(description);

        return transactionRepository.save(transaction);
    }

    @Transactional
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }


}

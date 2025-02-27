package com.example.petprojectbudgettracker.services;

import com.example.petprojectbudgettracker.models.*;
import com.example.petprojectbudgettracker.repositories.SubCategoryRepository;
import com.example.petprojectbudgettracker.repositories.TransactionRepository;
import com.example.petprojectbudgettracker.utils.SimpleImageProcessor;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

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

    public List<Transaction> getAllTransactionsForCurrentUser() {
        User user = authService.getCurrentUser();
        return transactionRepository.findByUserId(user.getId());
    }

    public Transaction getTransactionsById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    public List<Transaction> getAllTransactionsByCurrentUserAndType(String type) {
        User user = authService.getCurrentUser();
        return transactionRepository.findByUserIdAndType(user.getId(), TransactionType.valueOf(type));
    }

    public List<Transaction> getAllTransactionsByCurrentUserAndCategory(String categoryType) {
        User user = authService.getCurrentUser();
        return transactionRepository.findByUserIdAndCategory(user.getId(), TransactionCategory.valueOf(categoryType));
    }

    @Transactional
    public Transaction saveTransaction(Double amount, TransactionType type,
                                       TransactionCategory category, Long subCategoryId,
                                       String description, MultipartFile file) throws IOException {
        User user = authService.getCurrentUser();

        Double finalAmount = amount;

        SubCategory subCategory = subCategoryRepository.findById(subCategoryId)
                .orElseThrow(() -> new RuntimeException("SubCategory not found"));

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setType(type);
        transaction.setCategory(category);
        transaction.setSubCategory(subCategory);
        transaction.setTransactionDate(LocalDate.now());
        transaction.setDescription(description);

        if (file != null) {
            String imagePath = SimpleImageProcessor.saveImage(file);
            transaction.setImageUrl(imagePath);
            System.out.println("Saved image at: " + imagePath);

            String extractedAmount = SimpleImageProcessor.getTotalAmountFromImage(imagePath);
            if (extractedAmount != null) {
                try {
                    finalAmount = Double.parseDouble(extractedAmount);
                    transaction.setAmount(finalAmount);
                } catch (NumberFormatException e) {
                    System.err.println("Failed to parse extracted amount: " + extractedAmount);
                    transaction.setAmount(finalAmount != null ? finalAmount : 0.0);
                }
            } else {
                transaction.setAmount(finalAmount != null ? finalAmount : 0.0);
            }
        } else {
            transaction.setAmount(finalAmount != null ? finalAmount : 0.0);
        }


        if (type == TransactionType.INCOME) {
            user.setBudget(user.getBudget() + finalAmount);
        } else if (type == TransactionType.EXPENSE) {
            user.setBudget(user.getBudget() - finalAmount);
        }

        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction updateTransaction(Long id, Double amount, TransactionType type,
                                         TransactionCategory category, Long subCategoryId,
                                         String description) {
        User user = authService.getCurrentUser();

        Transaction transaction = getTransactionsById(id);

        if (transaction.getUser() != user) {
            throw new RuntimeException("Transaction not found");
        }

        if (amount != null) {
            double difference = amount - transaction.getAmount();
            transaction.setAmount(transaction.getAmount() + difference);
            user.setBudget(user.getBudget() + difference);
        }
        if (type != null) transaction.setType(type);
        if (category != null) transaction.setCategory(category);
        if (subCategoryId != null) {
            SubCategory subCategory = subCategoryRepository.findById(subCategoryId)
                    .orElseThrow(() -> new RuntimeException("SubCategory not found"));
            transaction.setSubCategory(subCategory);
        }
        if (description != null) transaction.setDescription(description);

        return transactionRepository.save(transaction);
    }

    @Transactional
    public void deleteTransaction(Long id) {
        User user = authService.getCurrentUser();
        Transaction transaction = getTransactionsById(id);
        if (transaction.getUser() != user) {
            throw new RuntimeException("Transaction not found");
        }
        user.setBudget(user.getBudget() + transaction.getAmount());
        transactionRepository.deleteById(id);
    }


}

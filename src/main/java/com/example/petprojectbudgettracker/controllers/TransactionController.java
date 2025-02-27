package com.example.petprojectbudgettracker.controllers;

import com.example.petprojectbudgettracker.models.Transaction;
import com.example.petprojectbudgettracker.models.TransactionCategory;
import com.example.petprojectbudgettracker.models.TransactionType;
import com.example.petprojectbudgettracker.services.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/get-all-transactions")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactionsForCurrentUser());
    }

    @GetMapping("/get-transaction-by-id")
    public ResponseEntity<Transaction> getTransactionById(@RequestParam Long id) {
        return ResponseEntity.ok(transactionService.getTransactionsById(id));
    }

    @GetMapping("/find-transactions-by-type")
    public ResponseEntity<List<Transaction>> findTransactionsByType(@RequestParam String type) {
        return ResponseEntity.ok(transactionService.getAllTransactionsByCurrentUserAndType(type));
    }

    @GetMapping("/find-transaction-by-category")
    public ResponseEntity<List<Transaction>> findTransactionsByCategory(@RequestParam String category) {
        return ResponseEntity.ok(transactionService.getAllTransactionsByCurrentUserAndCategory(category));
    }

    @PostMapping("/create-new-transaction")
    public ResponseEntity<Transaction> createTransaction(@RequestParam(required = false) Double amount,
                                                         @RequestParam TransactionType type,
                                                         @RequestParam TransactionCategory category,
                                                         @RequestParam Long subCategoryId,
                                                         @RequestParam(required = false) String description,
                                                         @RequestParam(required = false) MultipartFile file) throws IOException {
        return new ResponseEntity<>(transactionService.saveTransaction(amount, type
                , category, subCategoryId, description, file), HttpStatus.CREATED);
    }

    @PostMapping("/update-transaction")
    public ResponseEntity<Transaction> updateTransaction(@RequestParam Long id,
                                                         @RequestParam(required = false) Double amount,
                                                         @RequestParam(required = false) TransactionType type,
                                                         @RequestParam(required = false) TransactionCategory category,
                                                         @RequestParam(required = false) Long subCategoryId,
                                                         @RequestParam(required = false) String description) {
        return new ResponseEntity<>(transactionService.updateTransaction(id, amount, type
                , category, subCategoryId, description), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete-transaction")
    public ResponseEntity<Transaction> deleteTransaction(@RequestParam Long id) {
        transactionService.deleteTransaction(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

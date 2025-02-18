package com.example.petprojectbudgettracker.models;


public enum TransactionCategory {
    HOUSING("Housing & utilities"),
    FOOD("Food"),
    TRANSPORT("Transportation"),
    HEALTH("Health and medicine"),
    ENTERTAINMENT("Active recreation and hobbies"),
    SHOPPING("Shopping and goods"),
    EDUCATION(" Expenses for children and education"),
    PETS(" Expenses for pets"),
    FINANCE("Financial operations"),
    OTHER("Other spends");

    private final String displayName;

    private TransactionCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

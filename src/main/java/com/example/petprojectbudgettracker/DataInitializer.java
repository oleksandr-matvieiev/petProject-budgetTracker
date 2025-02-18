package com.example.petprojectbudgettracker;

import com.example.petprojectbudgettracker.models.Category;
import com.example.petprojectbudgettracker.models.SubCategory;
import com.example.petprojectbudgettracker.models.TransactionCategory;
import com.example.petprojectbudgettracker.repositories.CategoryRepository;
import com.example.petprojectbudgettracker.repositories.SubCategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataInitializer implements CommandLineRunner {
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;

    private static final Map<TransactionCategory, List<String>> SUBCATEGORIES_MAP = new HashMap<>();


    static {
        SUBCATEGORIES_MAP.put(TransactionCategory.HOUSING, Arrays.asList("Rent / Mortgage", "Electricity", "Water and sewerage",
                "Gas / Heating", "Internet", "Mobile communication", "House / apartment renovation"));
        SUBCATEGORIES_MAP.put(TransactionCategory.FOOD, Arrays.asList("Food products", "Cafes and restaurants", "Coffee / tea / snacks",
                "Fast food", "Alcohol / drinks"));
        SUBCATEGORIES_MAP.put(TransactionCategory.TRANSPORT, Arrays.asList("Fuel", "Car maintenance", "Parking / fines",
                "Public transportation", "Taxi", "Airline tickets / travel", "Hotels / rental housing"));
        SUBCATEGORIES_MAP.put(TransactionCategory.HEALTH, Arrays.asList("Medicines and vitamins", "Doctors / consultations", "Dentistry",
                "Glasses / lenses", "Fitness / gym", "Health insurance", "Leisure and entertainment"));
        SUBCATEGORIES_MAP.put(TransactionCategory.ENTERTAINMENT, Arrays.asList("Movies, theater, concerts", "Video games / Steam / PlayStation Store", "Books / magazines",
                " Hobbies", "Karaoke / clubs", "Attractions / water parks"));
        SUBCATEGORIES_MAP.put(TransactionCategory.SHOPPING, Arrays.asList("Clothing and shoes", "Household goods", "Electronics and gadgets",
                "Cosmetics and perfumes", "Gifts / souvenirs", "Jewelry / accessories"));
        SUBCATEGORIES_MAP.put(TransactionCategory.EDUCATION, Arrays.asList("School / training", "Courses / tutors", "Kindergarten",
                "Toys", "Sections / clubs"));
        SUBCATEGORIES_MAP.put(TransactionCategory.PETS, Arrays.asList("Food and accessories", "Veterinarian", "Grooming and care"));
        SUBCATEGORIES_MAP.put(TransactionCategory.FINANCE, Arrays.asList("Loans and borrowings", "Payment of debts", "Fines and taxes"));
        SUBCATEGORIES_MAP.put(TransactionCategory.OTHER, Arrays.asList("Charity / donations", " Holidays and birthdays", "Unexpected expenses"));
    }

    public DataInitializer(CategoryRepository categoryRepository, SubCategoryRepository subCategoryRepository) {
        this.categoryRepository = categoryRepository;
        this.subCategoryRepository = subCategoryRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        initializeCategories();
    }

    private void initializeCategories() {
        for (TransactionCategory categoryEnum : TransactionCategory.values()) {
            Category category = new Category();
            category.setTransactionCategory(categoryEnum);
            categoryRepository.save(category);

            List<String> subCategories = SUBCATEGORIES_MAP.get(categoryEnum);
            if (subCategories != null) {
                for (String subCategory : subCategories) {
                    SubCategory subCategoryObj = new SubCategory();
                    subCategoryObj.setName(subCategory);
                    subCategoryObj.setCategory(category);
                    subCategoryRepository.save(subCategoryObj);
                }
            }

        }
    }


}

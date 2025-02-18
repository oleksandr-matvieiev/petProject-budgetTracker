package com.example.petprojectbudgettracker.services;

import com.example.petprojectbudgettracker.models.Category;
import com.example.petprojectbudgettracker.models.SubCategory;
import com.example.petprojectbudgettracker.models.TransactionCategory;
import com.example.petprojectbudgettracker.repositories.CategoryRepository;
import com.example.petprojectbudgettracker.repositories.SubCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;


    public CategoryService(CategoryRepository categoryRepository, SubCategoryRepository subCategoryRepository) {
        this.categoryRepository = categoryRepository;
        this.subCategoryRepository = subCategoryRepository;
    }


    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public List<SubCategory> findAllSubCategoriesByCategoryName(String categoryName) {
        return subCategoryRepository.findAllByCategoryTransactionCategory(TransactionCategory.valueOf(categoryName));
    }

}

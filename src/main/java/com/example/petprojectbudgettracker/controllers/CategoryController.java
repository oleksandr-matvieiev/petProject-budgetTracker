package com.example.petprojectbudgettracker.controllers;

import com.example.petprojectbudgettracker.models.Category;
import com.example.petprojectbudgettracker.models.SubCategory;
import com.example.petprojectbudgettracker.services.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryService categoryService;


    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/get-all-category")
    public List<Category> getAllCategory() {
        return categoryService.findAll();
    }
    @GetMapping("/get-all-subCategory-by-category")
    public List<SubCategory> getAllSubCategoryByCategory(@RequestParam String category) {
        return categoryService.findAllSubCategoriesByCategoryName(category);
    }
}

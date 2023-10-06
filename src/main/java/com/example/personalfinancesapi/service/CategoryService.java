package com.example.personalfinancesapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.personalfinancesapi.model.Category;
import com.example.personalfinancesapi.repository.CategoryRepository;

@Service
public class CategoryService {
  @Autowired
  private CategoryRepository categoryRepository;

  public List<Category> allCategories() {
    return categoryRepository.findAll();
  }

  public Category createCategory(Category category) {
    return categoryRepository.save(category);
  }
  
}

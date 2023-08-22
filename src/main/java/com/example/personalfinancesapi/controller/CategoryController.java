package com.example.personalfinancesapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.personalfinancesapi.model.Category;
import com.example.personalfinancesapi.service.CategoryService;

@RestController
@RequestMapping("api/v1/categories")
public class CategoryController {
  @Autowired
  private CategoryService categoryService;

  @GetMapping
  public ResponseEntity<List<Category>> getAllCategories() {
    return new ResponseEntity<List<Category>>(categoryService.allCategories(), HttpStatus.OK); }
  
}

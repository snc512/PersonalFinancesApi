package com.example.personalfinancesapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.personalfinancesapi.service.ExpenseService;
import com.example.personalfinancesapi.model.Expense;
import com.example.personalfinancesapi.Exceptions.ExpenseNotFoundException;
import com.example.personalfinancesapi.dto.ExpenseWithCategoryDTO;
import com.example.personalfinancesapi.exception.ErrorResponse;
import com.example.personalfinancesapi.exception.InvalidDataException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;


@RestController
@RequestMapping("/api/v1/expenses")
public class ExpenseController {
    private static final Logger logger = LoggerFactory.getLogger(ExpenseService.class);

  @Autowired
  private ExpenseService expenseService; 
  
  @GetMapping
  public ResponseEntity<List<ExpenseWithCategoryDTO>> getAllExpensesWithCategory(@RequestParam(name = "sortBy", defaultValue = "referenceNumber") String sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "ASC") Sort.Direction sortDirection) {
    List<ExpenseWithCategoryDTO> expenseDTOs = expenseService.allExpensesWithCategory(sortBy, sortDirection);
    return new ResponseEntity<>(expenseDTOs, HttpStatus.OK);
  }


  @GetMapping("/{referenceNumber}")
  public ResponseEntity<Optional<Expense>> getSingleExpense(@PathVariable String referenceNumber) {
    return new ResponseEntity<Optional<Expense>>(expenseService.singleExpense(referenceNumber), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<Expense> createExpense(@RequestBody Map<String, Object> expenseData) {
    try {
        System.out.println("expenseData");
        System.out.println(expenseData);
        Expense createdExpense = expenseService.createExpense(expenseData);
        return new ResponseEntity<>(createdExpense, HttpStatus.CREATED);
    } catch (InvalidDataException ex) {
        throw new InvalidDataException(HttpStatus.BAD_REQUEST, ex.getMessage());
    } catch (Exception e) {
        logger.error("An error occurred: {}", e.getMessage(), e);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/{referenceNumber}/category")
  public ResponseEntity<Expense> updateExpenseCategory(
    @PathVariable String referenceNumber,
    @RequestParam("newCategoryId") String newCategoryId) {
    
    try {
        ObjectId newCategoryObjectId = new ObjectId(newCategoryId);
        Expense updatedExpense = expenseService.updateExpenseCategory(referenceNumber, newCategoryObjectId);
        return new ResponseEntity<>(updatedExpense, HttpStatus.OK);
    } catch (ExpenseNotFoundException e) {
        // Handle the case where the expense is not found
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (IllegalArgumentException e) {
        // Handle the case where the provided newCategoryId is not a valid ObjectId
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }
}
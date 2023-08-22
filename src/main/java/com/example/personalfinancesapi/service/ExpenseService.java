package com.example.personalfinancesapi.service;

import org.springframework.stereotype.Service;

import com.example.personalfinancesapi.Exceptions.ExpenseNotFoundException;
import com.example.personalfinancesapi.model.Expense;
import com.example.personalfinancesapi.repository.ExpenseRepository;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ExpenseService {

  @Autowired
  private ExpenseRepository expenseRepository;

  public List<Expense> allExpenses() {
    return expenseRepository.findAll();
  }
  public Optional<Expense> singleExpense(String referenceNumber) {
    return expenseRepository.findExpenseByReferenceNumber(referenceNumber);
  }

  public Expense createExpense(Map<String, Object> expenseData) {
    // Extract data from the expenseData map
    String cardLast4 = (String) expenseData.get("cardLast4");
    String transDateString = (String) expenseData.get("transDate");
    LocalDateTime transDate = LocalDateTime.parse(transDateString);
    String postDateString = (String) expenseData.get("postDate");
    LocalDateTime postDate = LocalDateTime.parse(postDateString);
    String referenceNumber = (String) expenseData.get("referenceNumber");
    String description = (String) expenseData.get("description");
    Double credits = (Double) expenseData.get("credits");
    Double charges = (Double) expenseData.get("charges");
    String categoryIdString = (String) expenseData.get("categoryId");
    ObjectId categoryId = new ObjectId(categoryIdString);

    // Create a new Expense object
    Expense expense = new Expense();
    expense.setCardLast4(cardLast4);
    expense.setTransDate(transDate);
    expense.setPostDate(postDate);
    expense.setReferenceNumber(referenceNumber);
    expense.setDescription(description);
    expense.setCredits(credits);
    expense.setCharges(charges);
    expense.setCategory(categoryId);

    // Save the expense to the database
    return expenseRepository.save(expense);
  }

  public Expense updateExpenseCategory(String referenceNumber, ObjectId newCategoryId) {
    Optional<Expense> optionalExpense = expenseRepository.findExpenseByReferenceNumber(referenceNumber);
    
    if (optionalExpense.isPresent()) {
      Expense expense = optionalExpense.get();
      expense.setCategory(newCategoryId);
      return expenseRepository.save(expense);
    } else {
      // Handle the case where the expense with the given ID doesn't exist
      throw new ExpenseNotFoundException("Expense not found with ID: " + referenceNumber);
    }
  }
}
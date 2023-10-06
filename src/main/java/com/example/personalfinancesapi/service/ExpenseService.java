package com.example.personalfinancesapi.service;

import org.springframework.stereotype.Service;

import com.example.personalfinancesapi.Exceptions.ExpenseNotFoundException;
import com.example.personalfinancesapi.exception.InvalidDataException;
import com.example.personalfinancesapi.model.Expense;
import com.example.personalfinancesapi.repository.ExpenseRepository;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class ExpenseService {
  private static final Logger logger = LoggerFactory.getLogger(ExpenseService.class);


  @Autowired
  private ExpenseRepository expenseRepository;

  @Autowired
  private Validator validator;

  public List<Expense> allExpenses() {
    return expenseRepository.findAll();
  }

  public Optional<Expense> singleExpense(String referenceNumber) {
    return expenseRepository.findExpenseByReferenceNumber(referenceNumber);
  }

  public LocalDateTime dateConverter(String dateString) {
    if (dateString == null) {
      return null;
    }
    try {
      return LocalDateTime.parse(dateString);
    } catch (DateTimeParseException e) {
      try {
        LocalDateTime date = LocalDateTime.of(LocalDate.parse(dateString), LocalTime.of(0, 0));
        return date;
      } catch (DateTimeParseException ex) {
        System.err.println("Error parsing date: " + ex.getMessage());
        return null; 
      }
    }
  }

  public Expense createExpense(Map<String, Object> expenseData) {
    // Extract data from the expenseData map


    String cardLast4 = (String) expenseData.get("cardLast4");
    String transDateString = (String) expenseData.get("transDate");
    LocalDateTime transDate = dateConverter(transDateString);
    String postDateString = (String) expenseData.get("postDate");
    LocalDateTime postDate = dateConverter(postDateString);
    String referenceNumber = (String) expenseData.get("referenceNumber");
    String description = (String) expenseData.get("description");
    Double credits = (Double) expenseData.get("credits");
    Double charges = (Double) expenseData.get("charges");
    String categoryIdString = (String) expenseData.get("categoryId");
    ObjectId categoryId = null;

    if (credits == null && charges == null) {
      System.out.println("Getting in here");
      throw new InvalidDataException(HttpStatus.BAD_REQUEST, "Either 'credits' or 'charges' must be non-null.");
    }

    if (categoryIdString != null && !categoryIdString.isEmpty()) {
        categoryId = new ObjectId(categoryIdString);
    }


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

    Set<ConstraintViolation<Expense>> violations = validator.validate(expense);
    if (!violations.isEmpty()) {
      logger.debug("Throwing InvalidDataException");
      throw new InvalidDataException(HttpStatus.BAD_REQUEST, "Invalid data in expense object");
    }

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
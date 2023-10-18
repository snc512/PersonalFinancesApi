package com.example.personalfinancesapi.service;

import org.springframework.stereotype.Service;

import com.example.personalfinancesapi.Exceptions.ExpenseNotFoundException;
import com.example.personalfinancesapi.dto.CategoryDTO;
import com.example.personalfinancesapi.dto.ExpenseWithCategoryDTO;
import com.example.personalfinancesapi.exception.InvalidDataException;
import com.example.personalfinancesapi.model.Category;
import com.example.personalfinancesapi.model.Expense;
import com.example.personalfinancesapi.repository.CategoryRepository;
import com.example.personalfinancesapi.repository.ExpenseRepository;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class ExpenseService {
  private static final Logger logger = LoggerFactory.getLogger(ExpenseService.class);


  @Autowired
  private ExpenseRepository expenseRepository;
  
  @Autowired
  private CategoryRepository categoryRepository;

  @Autowired
  private Validator validator;

  public List<Expense> allExpenses() {
    return expenseRepository.findAll();
  }

  public Optional<Expense> singleExpense(String referenceNumber) {
    return expenseRepository.findExpenseByReferenceNumber(referenceNumber);
  }

  private LocalDateTime dateConverter(String dateString) {
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
        logger.error("Error parsing date: " + ex.getMessage());
        return null; 
      }
    }
  }

  private static Double convertToDouble(Object value) {
    if (value == null) {
        return null;
    }

    if (value instanceof Double) {
        return (Double) value;
    }

    if (value instanceof Integer) {
        return ((Integer) value).doubleValue();
    }

    if (value instanceof String) {
        try {
            return Double.parseDouble((String) value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    return null;
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
    Double credits = convertToDouble(expenseData.get("credits"));

    Double charges = convertToDouble(expenseData.get("charges"));
    String categoryIdString = (String) expenseData.get("categoryId");
    ObjectId categoryId = null;

    if (credits == null && charges == null) {
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

  public List<ExpenseWithCategoryDTO> allExpensesWithCategory(String sortField, Sort.Direction sortDirection) {
    Sort sort = Sort.by(sortDirection, sortField);
    List<Expense> expenses = expenseRepository.findAll(sort);
    return expenses.stream()
        .map(this::mapToExpenseWithCategoryDTO)
        .collect(Collectors.toList());
  }

  private ExpenseWithCategoryDTO mapToExpenseWithCategoryDTO(Expense expense) {
    ExpenseWithCategoryDTO dto = new ExpenseWithCategoryDTO();
    // Map basic expense properties to DTO
    dto.setCardLast4(expense.getCardLast4());
    dto.setTransDate(expense.getTransDate());
    dto.setPostDate(expense.getPostDate());
    dto.setReferenceNumber(expense.getReferenceNumber());
    dto.setDescription(expense.getDescription());
    dto.setCredits(expense.getCredits());
    dto.setCharges(expense.getCharges());

    if (expense.getCategory() != null) {
      Optional<Category> categoryOptional = categoryRepository.findById(expense.getCategory());
  
  
      if (categoryOptional.isPresent()) {
        Category category = categoryOptional.get();
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName(category.getName()); 
  
        dto.setCategory(categoryDTO);
      }
    }

    return dto;
  }


}
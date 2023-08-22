package com.example.personalfinancesapi.repository;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.personalfinancesapi.model.Expense;

@Repository
public interface ExpenseRepository extends MongoRepository<Expense, ObjectId> {
  Optional<Expense> findExpenseByReferenceNumber(String referenceNumber);
}
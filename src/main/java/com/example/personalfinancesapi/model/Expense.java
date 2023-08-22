package com.example.personalfinancesapi.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "expenses")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Expense {
  @Id
  private ObjectId id;

  private String cardLast4;
  private LocalDateTime transDate;
  private LocalDateTime postDate;
  private String referenceNumber;
  private String description;
  private Double credits;
  private Double charges;

  // @DocumentReference
  private ObjectId category;
}
package com.example.personalfinancesapi.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.personalfinancesapi.validation.NonNullCreditOrCharge;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Document(collection = "expenses")
@Data
@AllArgsConstructor
@NoArgsConstructor
@NonNullCreditOrCharge
public class Expense {
  @Id
  private ObjectId id;
  @NotNull
  private String cardLast4;
  @NotNull
  private LocalDateTime transDate;
  @NotNull
  private LocalDateTime postDate;
  @NotNull
  private String referenceNumber;
  @NotNull
  private String description;
  private Double credits;
  private Double charges;
  private ObjectId category;
}

package com.example.personalfinancesapi.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime; 

@Document(collection="categories")
@Data 
@AllArgsConstructor
@NoArgsConstructor
public class Category {
  @Id
  private ObjectId id;

  private String name;
  private String icon;
  private String color;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
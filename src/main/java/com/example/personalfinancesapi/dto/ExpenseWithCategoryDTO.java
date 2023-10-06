package com.example.personalfinancesapi.dto;

import java.time.LocalDateTime;

public class ExpenseWithCategoryDTO {
    private String cardLast4;
    private LocalDateTime transDate;
    private LocalDateTime postDate;
    private String referenceNumber;
    private String description;
    private Double credits;
    private Double charges;
    private CategoryDTO category;

    public String getCardLast4() {
      return cardLast4;
    }

    public LocalDateTime getTransDate() {
      return transDate;
    }

    public LocalDateTime getPostDate() {
      return postDate;
    }

    public String getReferenceNumber() {
      return referenceNumber;
    }

    public String getDescription() {
      return description;
    }

    public Double getCredits() {
      return credits;
    }

    public Double getCharges() {
      return charges;
    }

    public CategoryDTO getCategory() {
      return category;
    }

    public void setCardLast4(String cardLast4) {
        this.cardLast4 = cardLast4;
    }

    public void setTransDate(LocalDateTime transDate) {
        this.transDate = transDate;
    }

    public void setPostDate(LocalDateTime postDate) {
        this.postDate = postDate;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCredits(Double credits) {
        this.credits = credits;
    }

    public void setCharges(Double charges) {
        this.charges = charges;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }
}

package com.example.personalfinancesapi.validation;
import com.example.personalfinancesapi.model.Expense;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NonNullCreditOrChargeValidator implements ConstraintValidator<NonNullCreditOrCharge, Expense> {
    @Override
    public void initialize(NonNullCreditOrCharge constraintAnnotation) {
    }

    @Override
    public boolean isValid(Expense expense, ConstraintValidatorContext context) {
        return expense.getCredits() != null || expense.getCharges() != null;
    }
}
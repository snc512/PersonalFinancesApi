package com.example.personalfinancesapi.validation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NonNullCreditOrChargeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NonNullCreditOrCharge {
    String message() default "Either 'credits' or 'charges' must be non-null";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

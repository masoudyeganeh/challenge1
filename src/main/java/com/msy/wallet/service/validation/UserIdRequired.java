package com.msy.wallet.service.validation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserIdValidator.class) // Specify the validator class
public @interface UserIdRequired {

    String message() default "userId is required"; // Default error message

    Class<?>[] groups() default {}; // Grouping constraints

    Class<? extends Payload>[] payload() default {}; // Payload for additional data
}

class UserIdValidator implements ConstraintValidator<UserIdRequired, Long> {

    @Override
    public void initialize(UserIdRequired constraintAnnotation) {
        // No initialization needed in this case
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        // Check if the userId is not null
        return value != null;
    }
}
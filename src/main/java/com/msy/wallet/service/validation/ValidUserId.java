package com.msy.wallet.service.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@NotNull(message = "User ID cannot be null")
@Min(value = 1, message = "User ID must be a positive number")
public @interface ValidUserId {
    String message() default "Invalid user ID";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

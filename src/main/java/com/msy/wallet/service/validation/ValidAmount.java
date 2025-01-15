package com.msy.wallet.service.validation;

import com.msy.wallet.exception.ErrorCode;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidAmountValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAmount {
    ErrorCode errorCode() default ErrorCode.Amount_MUST_BE_GREATOR_THAN_ZERO;
    String message() default "{errorCode}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

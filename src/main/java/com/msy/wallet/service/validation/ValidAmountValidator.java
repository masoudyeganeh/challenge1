package com.msy.wallet.service.validation;

import com.msy.wallet.exception.ErrorCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidAmountValidator implements ConstraintValidator<ValidAmount, Integer> {

    private ErrorCode errorCode;

    @Override
    public void initialize(ValidAmount constraintAnnotation) {
        this.errorCode = constraintAnnotation.errorCode();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            setErrorMessage(context, ErrorCode.AMOUNT_REQUIRED);
            return false;
        }
        if (value < 1) {
            setErrorMessage(context, ErrorCode.Amount_MUST_BE_GREATOR_THAN_ZERO);
            return false;
        }
        return true;
    }

    private void setErrorMessage(ConstraintValidatorContext context, ErrorCode errorCode) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(errorCode.getMessage()).addConstraintViolation();
    }
}

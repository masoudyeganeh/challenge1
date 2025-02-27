package com.msy.wallet.exception;

public enum ErrorCode {
    USER_NOT_FOUND(1001, "User not found with ID: %s"),
    PARAMETER_IS_REQUIRED(1002, "%s is required"),
    INVALID_INPUT(1003, "Invalid value for parameter %s . Expected type: %s"),

    INSUFFICIENT_BALANCE(1004, "Insufficient balance"),

    OptimisticLockException(1005, "Balance is modified by another user. Please try again!"),

    UNEXPECTED_ERROR(1006, "An unexpected error has occured. Please try again!"),

    ZERO_AMOUNT_NOT_ALLOWED(1007, "Amount should be greater than 0");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getFormattedMessage(Object... args) {
        return String.format(message, args);
    }
}

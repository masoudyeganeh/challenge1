package com.msy.wallet.exception;

public enum ErrorCode {
    USER_NOT_FOUND(1001, "User not found with ID: %s"),
    INVALID_AMOUNT(1002, "Invalid amount: %s"),
    INSUFFICIENT_BALANCE(1003, "Insufficient balance for user ID: %s");

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

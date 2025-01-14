package com.msy.wallet.exception;

public class WalletServiceException extends RuntimeException {
    private final ErrorCode errorCode;
    private final Object[] args;

    public WalletServiceException(ErrorCode errorCode, Object... args) {
        super(errorCode.getFormattedMessage(args));
        this.errorCode = errorCode;
        this.args = args;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public Object[] getArgs() {
        return args;
    }
}

package com.msy.wallet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WalletServiceException.class)
    public ResponseEntity<?> handleWalletServiceException(WalletServiceException ex, WebRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("errorCode", ex.getErrorCode().getCode());
        response.put("errorMessage", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}

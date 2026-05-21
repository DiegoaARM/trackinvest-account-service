package com.trackinvest.account.wallet.infrastructure.handler;

import com.trackinvest.account.common.application.dto.ApiResponse;
import com.trackinvest.account.wallet.domain.exception.business.*;
import com.trackinvest.account.wallet.domain.exception.format.WalletAmountInvalidException;
import com.trackinvest.account.wallet.domain.exception.format.WalletNameInvalidException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.trackinvest.account.wallet")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WalletExceptionHandler {

    @ExceptionHandler(WalletNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleWalletNotFound(WalletNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage(), null));
    }

    @ExceptionHandler(WalletNameInvalidException.class)
    public ResponseEntity<ApiResponse<Void>> handleWalletNameInvalid(WalletNameInvalidException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage(), null));
    }

    @ExceptionHandler(WalletCannotDeleteLastException.class)
    public ResponseEntity<ApiResponse<Void>> handleWalletCannotDeleteLast(WalletCannotDeleteLastException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage(), null));
    }

    @ExceptionHandler(WalletInsufficientBalanceException.class)
    public ResponseEntity<ApiResponse<Void>> handleWalletInsufficientBalance(WalletInsufficientBalanceException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage(), null));
    }

    @ExceptionHandler(WalletAmountInvalidException.class)
    public ResponseEntity<ApiResponse<Void>> handleWalletInvalidBalance(WalletAmountInvalidException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage(), null));
    }

    @ExceptionHandler(WalletMaxNumberException.class)
    public ResponseEntity<ApiResponse<Void>> handleWalletMaxNumber(WalletMaxNumberException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage(), null));
    }

    @ExceptionHandler(WalletNameDuplicateException.class)
    public ResponseEntity<ApiResponse<Void>> handleWalletNameDuplicate(WalletNameDuplicateException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage(), null));
    }
}

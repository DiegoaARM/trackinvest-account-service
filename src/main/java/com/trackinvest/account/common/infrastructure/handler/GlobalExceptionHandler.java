package com.trackinvest.account.common.infrastructure.handler;

import com.trackinvest.account.common.application.dto.ApiResponse;
import com.trackinvest.account.common.domain.exception.TrackinvestException;
import com.trackinvest.account.common.domain.exception.RequiredAttributeException;
import com.trackinvest.account.common.domain.exception.ResourceAccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class GlobalExceptionHandler {

    @ExceptionHandler(RequiredAttributeException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationExceptions(TrackinvestException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) // 400 Bad Request
                .body(ApiResponse.error(ex.getMessage(), null));
    }

    @ExceptionHandler(ResourceAccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceAccessDenied(ResourceAccessDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(ex.getMessage(), null));
    }

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleAnyError(Exception ex) {
        log.error("Unhandled exception: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("An unexpected error occurred", null));
    }
}

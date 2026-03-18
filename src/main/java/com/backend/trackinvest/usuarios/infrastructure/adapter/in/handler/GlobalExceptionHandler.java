//package com.backend.trackinvest.usuarios.infrastructure.adapter.in.handler;
//
//import com.backend.trackinvest.usuarios.domain.exception.format.NameInvalidException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import java.util.Map;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(NameInvalidException.class)
//    public ResponseEntity<Map<String, String>> handleNameInvalid(NameInvalidException ex) {
//        return ResponseEntity
//                .status(HttpStatus.BAD_REQUEST)
//                .body(Map.of("error", ex.getMessage()));
//    }
//}

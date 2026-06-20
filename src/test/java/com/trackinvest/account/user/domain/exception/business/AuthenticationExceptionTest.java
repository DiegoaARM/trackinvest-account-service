package com.trackinvest.account.user.domain.exception.business;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationExceptionTest {

    @Test
    void shouldCreateWithMessage() {
        String message = "Authentication failed";
        AuthenticationException exception = new AuthenticationException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldCreateWithMessageAndCause() {
        String message = "Authentication failed";
        Throwable cause = new RuntimeException("root cause");
        AuthenticationException exception = new AuthenticationException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}

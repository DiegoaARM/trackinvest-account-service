package com.trackinvest.account.user.domain.rules;

import com.trackinvest.account.common.domain.exception.RequiredAttributeException;
import com.trackinvest.account.user.domain.models.UserDomain;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserValidatorRuleTest {

    private final UserValidatorRule validator = new UserValidatorRule();
    private final UUID id = UUID.randomUUID();
    private final LocalDateTime now = LocalDateTime.now();
    private final UserDomain validUser = UserDomain.from(id, "cognito-123", "Test User", "email@test.com", now, now, new ArrayList<>());

    @Test
    void shouldPassForValidUser() {
        assertDoesNotThrow(() -> validator.validate(validUser));
    }

    @Test
    void shouldThrowWhenUserIsNull() {
        assertThrows(RequiredAttributeException.class, () -> validator.validate(null));
    }

    @Test
    void shouldThrowWhenCognitoIdIsNull() {
        UserDomain user = UserDomain.create(id);
        assertThrows(RequiredAttributeException.class, () -> validator.validate(user));
    }

    @Test
    void shouldThrowWhenCognitoIdIsBlank() {
        UserDomain user = UserDomain.from(id, "   ", "Test User", "email@test.com", now, now, new ArrayList<>());
        assertThrows(RequiredAttributeException.class, () -> validator.validate(user));
    }

    @Test
    void shouldThrowWhenFullnameIsNull() {
        UserDomain user = UserDomain.create(id);
        assertThrows(RequiredAttributeException.class, () -> validator.validate(user));
    }

    @Test
    void shouldThrowWhenFullnameIsBlank() {
        UserDomain user = UserDomain.from(id, "cognito-123", "   ", "email@test.com", now, now, new ArrayList<>());
        assertThrows(RequiredAttributeException.class, () -> validator.validate(user));
    }

    @Test
    void shouldThrowWhenEmailIsNull() {
        UserDomain user = UserDomain.create(id);
        assertThrows(RequiredAttributeException.class, () -> validator.validate(user));
    }

    @Test
    void shouldThrowWhenEmailIsBlank() {
        UserDomain user = UserDomain.from(id, "cognito-123", "Test User", "   ", now, now, new ArrayList<>());
        assertThrows(RequiredAttributeException.class, () -> validator.validate(user));
    }

    @Test
    void shouldThrowWhenCreatedAtIsNull() {
        UserDomain user = UserDomain.create(id);
        assertThrows(RequiredAttributeException.class, () -> validator.validate(user));
    }

    @Test
    void shouldThrowWhenUpdatedAtIsNull() {
        UserDomain user = UserDomain.create(id);
        assertThrows(RequiredAttributeException.class, () -> validator.validate(user));
    }
}

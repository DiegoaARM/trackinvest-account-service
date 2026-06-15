package com.trackinvest.account.user.domain.rules;

import com.trackinvest.account.user.domain.exception.format.UserNameInvalidException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserNameValidRuleTest {

    @Test
    void shouldPassForValidName() {
        String name = "Valid Name";
        assertDoesNotThrow(() -> UserNameValidRule.validate(name));
    }

    @Test
    void shouldPassForValidNameWithNumbers() {
        String name = "User 123";
        assertDoesNotThrow(() -> UserNameValidRule.validate(name));
    }

    @Test
    void shouldThrowWhenNameIsNull() {
        assertThrows(UserNameInvalidException.class, () -> UserNameValidRule.validate(null));
    }

    @Test
    void shouldThrowWhenNameIsTooShort() {
        String name = "AB";
        assertThrows(UserNameInvalidException.class, () -> UserNameValidRule.validate(name));
    }

    @Test
    void shouldThrowWhenNameIsTooLong() {
        String name = "A".repeat(51);
        assertThrows(UserNameInvalidException.class, () -> UserNameValidRule.validate(name));
    }

    @Test
    void shouldThrowWhenNameHasSpecialCharacters() {
        String name = "Invalid@Name!";
        assertThrows(UserNameInvalidException.class, () -> UserNameValidRule.validate(name));
    }

    @Test
    void shouldThrowWhenNameHasUnderscore() {
        String name = "invalid_name";
        assertThrows(UserNameInvalidException.class, () -> UserNameValidRule.validate(name));
    }

    @Test
    void shouldPassForNameWithMultipleSpaces() {
        String name = "Name  With  Spaces";
        assertDoesNotThrow(() -> UserNameValidRule.validate(name));
    }
}

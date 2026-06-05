package com.trackinvest.account.user.domain.rules;

import com.trackinvest.account.user.domain.exception.format.UserNameInvalidException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserNameValidRuleTest {

    @Test
    void shouldAcceptValidNames() {
        assertDoesNotThrow(() -> UserNameValidRule.validate("John Doe"));
        assertDoesNotThrow(() -> UserNameValidRule.validate("Alice"));
        assertDoesNotThrow(() -> UserNameValidRule.validate("A".repeat(50)));
        assertDoesNotThrow(() -> UserNameValidRule.validate("A".repeat(3)));
    }

    @Test
    void shouldThrowWhenNameIsNull() {
        assertThrows(UserNameInvalidException.class, () ->
                UserNameValidRule.validate(null));
    }

    @Test
    void shouldThrowWhenNameIsTooShort() {
        assertThrows(UserNameInvalidException.class, () ->
                UserNameValidRule.validate("AB"));
    }

    @Test
    void shouldThrowWhenNameIsTooLong() {
        assertThrows(UserNameInvalidException.class, () ->
                UserNameValidRule.validate("A".repeat(51)));
    }

    @Test
    void shouldThrowWhenNameContainsSpecialCharacters() {
        assertThrows(UserNameInvalidException.class, () ->
                UserNameValidRule.validate("John!"));
        assertThrows(UserNameInvalidException.class, () ->
                UserNameValidRule.validate("John@Doe"));
        assertThrows(UserNameInvalidException.class, () ->
                UserNameValidRule.validate("John-Doe"));
    }
}

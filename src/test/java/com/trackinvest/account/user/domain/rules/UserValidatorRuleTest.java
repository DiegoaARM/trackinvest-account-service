package com.trackinvest.account.user.domain.rules;

import com.trackinvest.account.common.domain.exception.RequiredAttributeException;
import com.trackinvest.account.user.domain.models.UserDomain;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserValidatorRuleTest {

    private final UserValidatorRule validator = new UserValidatorRule();
    private final UUID id = UUID.randomUUID();
    private final LocalDateTime now = LocalDateTime.now();

    @Test
    void shouldPassForValidUser() {
        UserDomain user = UserDomain.from(id, "cognito-123", "Test User", "email@test.com", now, now, new ArrayList<>());
        assertDoesNotThrow(() -> validator.validate(user));
    }

    @Test
    void shouldThrowWhenUserIsNull() {
        assertThrows(RequiredAttributeException.class, () -> validator.validate(null));
    }

    @Test
    void shouldThrowWhenIdIsNull() {
        var user = mock(UserDomain.class);
        when(user.getId()).thenReturn(null);

        assertThrows(RequiredAttributeException.class, () -> validator.validate(user));
    }

    @Test
    void shouldThrowWhenCognitoIdIsNull() {
        var user = mock(UserDomain.class);
        when(user.getId()).thenReturn(id);
        when(user.getCognitoId()).thenReturn(null);

        assertThrows(RequiredAttributeException.class, () -> validator.validate(user));
    }

    @Test
    void shouldThrowWhenCognitoIdIsBlank() {
        var user = mock(UserDomain.class);
        when(user.getId()).thenReturn(id);
        when(user.getCognitoId()).thenReturn("   ");

        assertThrows(RequiredAttributeException.class, () -> validator.validate(user));
    }

    @Test
    void shouldThrowWhenFullnameIsNull() {
        var user = mock(UserDomain.class);
        when(user.getId()).thenReturn(id);
        when(user.getCognitoId()).thenReturn("cognito-123");
        when(user.getFullname()).thenReturn(null);

        assertThrows(RequiredAttributeException.class, () -> validator.validate(user));
    }

    @Test
    void shouldThrowWhenFullnameIsBlank() {
        var user = mock(UserDomain.class);
        when(user.getId()).thenReturn(id);
        when(user.getCognitoId()).thenReturn("cognito-123");
        when(user.getFullname()).thenReturn("   ");

        assertThrows(RequiredAttributeException.class, () -> validator.validate(user));
    }

    @Test
    void shouldThrowWhenEmailIsNull() {
        var user = mock(UserDomain.class);
        when(user.getId()).thenReturn(id);
        when(user.getCognitoId()).thenReturn("cognito-123");
        when(user.getFullname()).thenReturn("Test User");
        when(user.getEmail()).thenReturn(null);

        assertThrows(RequiredAttributeException.class, () -> validator.validate(user));
    }

    @Test
    void shouldThrowWhenEmailIsBlank() {
        var user = mock(UserDomain.class);
        when(user.getId()).thenReturn(id);
        when(user.getCognitoId()).thenReturn("cognito-123");
        when(user.getFullname()).thenReturn("Test User");
        when(user.getEmail()).thenReturn("   ");

        assertThrows(RequiredAttributeException.class, () -> validator.validate(user));
    }

    @Test
    void shouldThrowWhenCreatedAtIsNull() {
        var user = mock(UserDomain.class);
        when(user.getId()).thenReturn(id);
        when(user.getCognitoId()).thenReturn("cognito-123");
        when(user.getFullname()).thenReturn("Test User");
        when(user.getEmail()).thenReturn("email@test.com");
        when(user.getCreatedAt()).thenReturn(null);

        assertThrows(RequiredAttributeException.class, () -> validator.validate(user));
    }

    @Test
    void shouldThrowWhenUpdatedAtIsNull() {
        var user = mock(UserDomain.class);
        when(user.getId()).thenReturn(id);
        when(user.getCognitoId()).thenReturn("cognito-123");
        when(user.getFullname()).thenReturn("Test User");
        when(user.getEmail()).thenReturn("email@test.com");
        when(user.getCreatedAt()).thenReturn(now);
        when(user.getUpdatedAt()).thenReturn(null);

        assertThrows(RequiredAttributeException.class, () -> validator.validate(user));
    }

    @Test
    void shouldThrowWhenWalletsListIsNull() {
        var user = mock(UserDomain.class);
        when(user.getId()).thenReturn(id);
        when(user.getCognitoId()).thenReturn("cognito-123");
        when(user.getFullname()).thenReturn("Test User");
        when(user.getEmail()).thenReturn("email@test.com");
        when(user.getCreatedAt()).thenReturn(now);
        when(user.getUpdatedAt()).thenReturn(now);
        when(user.getWalletsList()).thenReturn(null);

        assertThrows(RequiredAttributeException.class, () -> validator.validate(user));
    }
}

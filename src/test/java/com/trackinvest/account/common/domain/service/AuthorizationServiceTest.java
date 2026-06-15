package com.trackinvest.account.common.domain.service;

import com.trackinvest.account.common.domain.exception.ResourceAccessDeniedException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AuthorizationServiceTest {

    private final AuthorizationService authorizationService = new AuthorizationService();

    @Test
    void shouldPassWhenUserIsOwner() {
        UUID ownerId = UUID.randomUUID();

        assertDoesNotThrow(() ->
                authorizationService.verifyOwner(ownerId, ownerId, "wallet"));
    }

    @Test
    void shouldThrowWhenOwnerIsNull() {
        UUID contextUserId = UUID.randomUUID();

        assertThrows(ResourceAccessDeniedException.class, () ->
                authorizationService.verifyOwner(null, contextUserId, "wallet"));
    }

    @Test
    void shouldThrowWhenUserIsNotOwner() {
        UUID ownerId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();

        assertThrows(ResourceAccessDeniedException.class, () ->
                authorizationService.verifyOwner(ownerId, otherUserId, "wallet"));
    }
}

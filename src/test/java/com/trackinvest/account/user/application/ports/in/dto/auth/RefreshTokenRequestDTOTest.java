package com.trackinvest.account.user.application.ports.in.dto.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RefreshTokenRequestDTOTest {

    @Test
    void shouldCreateRecordWithRefreshToken() {
        String token = "refresh-token-value";
        RefreshTokenRequestDTO dto = new RefreshTokenRequestDTO(token);

        assertEquals(token, dto.refreshToken());
    }

    @Test
    void shouldCreateRecordWithNullRefreshToken() {
        RefreshTokenRequestDTO dto = new RefreshTokenRequestDTO(null);

        assertNull(dto.refreshToken());
    }
}

package com.trackinvest.account.user.application.usecase.auth;

import com.trackinvest.account.user.application.ports.in.dto.auth.TokenDTO;
import com.trackinvest.account.user.application.ports.out.IdentityProviderPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenUseCaseTest {

    @Mock
    private IdentityProviderPort identityProvider;

    @InjectMocks
    private RefreshTokenUseCase refreshTokenUseCase;

    @Test
    void shouldReturnNewTokens() {
        TokenDTO expectedToken = new TokenDTO("new-access", "new-refresh", "new-id", 3600L);
        when(identityProvider.refreshTokens("valid-refresh-token")).thenReturn(expectedToken);

        TokenDTO result = refreshTokenUseCase.execute("valid-refresh-token");

        assertNotNull(result);
        assertEquals("new-access", result.access_token());
        assertEquals("new-refresh", result.refresh_token());
        assertEquals("new-id", result.id_token());
        assertEquals(3600L, result.expires_in());
        verify(identityProvider).refreshTokens("valid-refresh-token");
    }
}

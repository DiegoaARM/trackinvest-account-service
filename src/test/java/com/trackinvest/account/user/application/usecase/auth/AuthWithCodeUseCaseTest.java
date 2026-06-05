package com.trackinvest.account.user.application.usecase.auth;

import com.trackinvest.account.user.application.ports.in.dto.auth.TokenDTO;
import com.trackinvest.account.user.application.ports.in.service.user.SyncUserPort;
import com.trackinvest.account.user.application.ports.out.IdentityProviderPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthWithCodeUseCaseTest {

    @Mock
    private IdentityProviderPort identityProvider;

    @Mock
    private SyncUserPort syncUserPort;

    @InjectMocks
    private AuthWithCodeUseCase authWithCodeUseCase;

    @Test
    void shouldExchangeCodeAndSyncUser() {
        String headerJson = "{\"alg\":\"HS256\"}";
        String payloadJson = "{\"sub\":\"cognito-sub-123\",\"email\":\"user@test.com\",\"name\":\"Test User\"}";
        String encodedHeader = Base64.getUrlEncoder().withoutPadding().encodeToString(headerJson.getBytes(StandardCharsets.UTF_8));
        String encodedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(payloadJson.getBytes(StandardCharsets.UTF_8));
        String idToken = encodedHeader + "." + encodedPayload + ".signature";

        TokenDTO token = new TokenDTO("access-token", "refresh-token", idToken, 3600L);
        when(identityProvider.exchangeCodeForToken("auth-code")).thenReturn(token);

        TokenDTO result = authWithCodeUseCase.execute("auth-code");

        assertNotNull(result);
        assertEquals("access-token", result.access_token());
        assertEquals(idToken, result.id_token());
        verify(syncUserPort).execute("cognito-sub-123", "user@test.com", "Test User");
    }
}

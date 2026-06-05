package com.trackinvest.account.user.application.usecase.auth;

import com.trackinvest.account.user.application.ports.in.dto.auth.UrlDTO;
import com.trackinvest.account.user.application.ports.out.IdentityProviderPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenerateAuthUrlUseCaseTest {

    @Mock
    private IdentityProviderPort identityProvider;

    @InjectMocks
    private GenerateAuthUrlUseCase generateAuthUrlUseCase;

    @Test
    void shouldReturnAuthorizationUrl() {
        String expectedUrl = "https://cognito.auth.amazon.com/login";
        when(identityProvider.generateAuthorizationUrl()).thenReturn(expectedUrl);

        UrlDTO result = generateAuthUrlUseCase.execute();

        assertNotNull(result);
        assertEquals(expectedUrl, result.url());
        verify(identityProvider).generateAuthorizationUrl();
    }
}

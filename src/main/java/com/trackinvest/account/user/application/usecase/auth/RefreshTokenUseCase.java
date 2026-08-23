package com.trackinvest.account.user.application.usecase.auth;

import com.trackinvest.account.user.application.ports.in.dto.auth.TokenDTO;
import com.trackinvest.account.user.application.ports.in.service.auth.RefreshTokenPort;
import com.trackinvest.account.user.application.ports.out.IdentityProviderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenUseCase implements RefreshTokenPort {

    private final IdentityProviderPort identityProvider;

    @Override
    public TokenDTO execute(String refreshToken) {
        // Call the adapter to obtain new tokens
        TokenDTO response = identityProvider.refreshTokens(refreshToken);

        return response;
    }
}

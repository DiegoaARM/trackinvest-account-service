package com.backend.trackinvest.usuarios.application.usecase;

import com.backend.trackinvest.usuarios.application.ports.in.dto.auth.TokenDTO;
import com.backend.trackinvest.usuarios.application.ports.in.dto.auth.UrlDTO;
import com.backend.trackinvest.usuarios.application.ports.in.service.AuthPort;
import com.backend.trackinvest.usuarios.application.ports.out.IdentityProviderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthUseCase implements AuthPort {

    private final IdentityProviderPort identityProvider;

    @Override
    public UrlDTO getAuthUrl() {
        return new UrlDTO(identityProvider.generateAuthorizationUrl());
    }

    @Override
    public TokenDTO authenticateWithCode(String code) {
        String idToken = identityProvider.exchangeCodeForToken(code);
        // Aquí podrías llamar a un UserSyncService para guardar/actualizar el usuario en tu DB local
        return new TokenDTO(idToken);
    }
}

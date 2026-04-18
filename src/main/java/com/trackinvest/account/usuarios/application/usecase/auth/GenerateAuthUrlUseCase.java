package com.trackinvest.account.usuarios.application.usecase.auth;

import com.trackinvest.account.usuarios.application.ports.in.dto.auth.UrlDTO;
import com.trackinvest.account.usuarios.application.ports.in.service.auth.GenerateAuthUrlPort;
import com.trackinvest.account.usuarios.application.ports.out.IdentityProviderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GenerateAuthUrlUseCase implements GenerateAuthUrlPort {

    private final IdentityProviderPort identityProvider;

    @Override
    public UrlDTO execute() {
        return new UrlDTO(identityProvider.generateAuthorizationUrl());
    }
}

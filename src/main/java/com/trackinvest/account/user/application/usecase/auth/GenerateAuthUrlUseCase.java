package com.trackinvest.account.user.application.usecase.auth;

import com.trackinvest.account.user.application.ports.in.dto.auth.UrlDTO;
import com.trackinvest.account.user.application.ports.in.service.auth.GenerateAuthUrlPort;
import com.trackinvest.account.user.application.ports.out.IdentityProviderPort;
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

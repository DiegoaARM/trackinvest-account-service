package com.trackinvest.account.usuarios.application.usecase.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.trackinvest.account.usuarios.application.ports.in.dto.auth.TokenDTO;
import com.trackinvest.account.usuarios.application.ports.in.service.auth.AuthWithCodePort;
import com.trackinvest.account.usuarios.application.ports.in.service.user.SyncUserPort;
import com.trackinvest.account.usuarios.application.ports.out.IdentityProviderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthWithCodeUseCase implements AuthWithCodePort {

    private final IdentityProviderPort identityProvider;
    private final SyncUserPort syncUserPort;

    @Override
    public TokenDTO execute(String code) {
        String idToken = identityProvider.exchangeCodeForToken(code);

        DecodedJWT jwt = JWT.decode(idToken);

        String cognitoId = jwt.getSubject(); // El 'sub'
        String email = jwt.getClaim("email").asString();
        String fullName = jwt.getClaim("name").asString();

        System.out.println("Cognito ID: " + cognitoId);
        System.out.println("Email: " + email);
        System.out.println("Full Name: " + fullName);

        syncUserPort.execute(cognitoId, email, fullName);

        return new TokenDTO(idToken);
    }
}

package com.trackinvest.account.usuarios.application.ports.out;

public interface IdentityProviderPort {
    String generateAuthorizationUrl();
    String exchangeCodeForToken(String code);
}

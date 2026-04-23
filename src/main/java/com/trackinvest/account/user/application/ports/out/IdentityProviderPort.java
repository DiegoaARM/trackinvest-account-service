package com.trackinvest.account.user.application.ports.out;

public interface IdentityProviderPort {
    String generateAuthorizationUrl();
    String exchangeCodeForToken(String code);
}

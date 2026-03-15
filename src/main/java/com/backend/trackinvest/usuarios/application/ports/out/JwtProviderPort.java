package com.backend.trackinvest.usuarios.application.ports.out;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.Authentication;

public interface JwtProviderPort {

    String createToken(Authentication authentication);
    DecodedJWT validateToken(String token);
    String extractUserName(DecodedJWT decodedJWT);
}

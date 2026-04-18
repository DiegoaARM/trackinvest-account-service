//package com.backend.trackinvest.usuarios.infrastructure.adapter.out.security;
//
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.auth0.jwt.exceptions.JWTVerificationException;
//import com.auth0.jwt.interfaces.DecodedJWT;
//import com.auth0.jwt.interfaces.JWTVerifier;
//import com.backend.trackinvest.usuarios.application.ports.out.JwtProviderPort;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//@Component
//public class JwtAdapter implements JwtProviderPort {
//
//    @Value("${security.jwt.key.private}")
//    private String privateKey;
//
//    @Value("${security.jwt.user.generator}")
//    private String userGenerator;
//
//    // 8 hours
//    private static final long EXPIRATION_TIME = 1000L * 60L * 60L * 8L;
//
//    @Override
//    public String createToken(Authentication authentication) {
//        Algorithm algorithm = Algorithm.HMAC256(this.privateKey);
//
//        // Get username from authentication
//        String username = authentication.getName();
//
//        // Mapping roles
//        String authorities = authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.joining(","));
//
//        return JWT.create()
//                .withIssuer(this.userGenerator)
//                .withSubject(username)
//                .withClaim("authorities", authorities) // Corregido el typo a 'authorities'
//                .withIssuedAt(new Date())
//                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//                .withJWTId(UUID.randomUUID().toString())
//                .sign(algorithm);
//    }
//
//    @Override
//    public DecodedJWT validateToken(String token) {
//        try {
//            Algorithm algorithm = Algorithm.HMAC256(this.privateKey);
//            JWTVerifier verifier = JWT.require(algorithm)
//                    .withIssuer(this.userGenerator)
//                    .build();
//
//            return verifier.verify(token);
//        } catch (JWTVerificationException exception) {
//            throw new JWTVerificationException("Invalid JWT token");
//        }
//    }
//
//
//    @Override
//    public String extractUserName(DecodedJWT decodedJWT) {
//        return decodedJWT.getSubject();
//    }
//}

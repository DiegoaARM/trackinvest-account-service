//package com.backend.trackinvest.usuarios.infrastructure.adapter.in.handler;
//
//import com.auth0.jwt.exceptions.JWTVerificationException;
//import com.auth0.jwt.exceptions.TokenExpiredException;
//import com.auth0.jwt.interfaces.DecodedJWT;
//import com.backend.trackinvest.usuarios.application.ports.out.JwtProviderPort;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpHeaders;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.Collection;
//import java.util.Collections;
//
//@Component
//@RequiredArgsConstructor
//public class JwtFilter extends OncePerRequestFilter {
//
//    private final JwtProviderPort jwtProvider;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain)
//            throws ServletException, IOException {
//
//        // 1. Get auth header
//        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);
//
//        if (jwtToken == null || !jwtToken.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        try {
//            jwtToken = jwtToken.substring(7);
//            DecodedJWT decodedJWT = jwtProvider.validateToken(jwtToken);
//
//            String userName = jwtProvider.extractUserName(decodedJWT);
//            // Extraemos los roles del claim 'authorities'
//            String stringAuthorities = decodedJWT.getClaim("authorities").asString();
//
//            Collection<GrantedAuthority> authorities = AuthorityUtils
//                    .commaSeparatedStringToAuthorityList(stringAuthorities);
//
//            UsernamePasswordAuthenticationToken authentication =
//                    new UsernamePasswordAuthenticationToken(userName, null, authorities);
//
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        } catch (TokenExpiredException ex) {
//            sendErrorResponse(response, "TOKEN_EXPIRED", "Tu sesión ha expirado.");
//            return;
//        } catch (JWTVerificationException ex) {
//            sendErrorResponse(response, "TOKEN_INVALID", "Token no autorizado.");
//            return;
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
//    private void sendErrorResponse(HttpServletResponse response, String code, String message) throws IOException {
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.setContentType("application/json");
//        response.getWriter().write(String.format(
//                "{\"errorCode\": \"%s\", \"message\": \"%s\"}", code, message
//        ));
//    }
//}

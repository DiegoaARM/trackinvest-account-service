package com.trackinvest.account.common.infrastructure.handler;

import com.trackinvest.account.user.infrastructure.adapter.out.persistence.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@AllArgsConstructor
public class UserContextInterceptor implements HandlerInterceptor {

    private final UserRepository userRepository;
    private final Map<String, UUID> idCache = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            String cognitoId = jwt.getSubject();

            // computeIfAbsent: if not in cache, query DB and store; if present, skip DB query.
            UUID userId = idCache.computeIfAbsent(cognitoId, key ->
                    userRepository.findIdByCognitoId(key).orElse(null)
            );

            if (userId == null) {
                throw new RuntimeException();
            }
            request.setAttribute("USER_ID", userId);
        }
        return true;
    }
}

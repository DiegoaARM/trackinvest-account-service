package com.backend.trackinvest.usuarios.infrastructure.adapter.in.controller;

import com.backend.trackinvest.usuarios.application.ports.in.dto.user.GetUserResponseDTO;
import com.backend.trackinvest.usuarios.application.ports.in.service.user.GetMePort;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "Endpoints para gestión de perfil de usuario")
public class UserController {

    private final GetMePort getMePort;

    @GetMapping("/me")
    public ResponseEntity<GetUserResponseDTO> getMe(@AuthenticationPrincipal Jwt jwt) {
        // 'jwt.getSubject()' extrae el "sub" de Cognito (el ID único)
        String cognitoId = jwt.getSubject();

        // Llamamos al caso de uso que ya devuelve el DTO limpio
        GetUserResponseDTO userDTO = getMePort.execute(cognitoId);

        return ResponseEntity.ok(userDTO);
    }
}

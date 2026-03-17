package com.backend.trackinvest.usuarios.infrastructure.adapter.in.controller;

import com.auth0.jwt.JWT;
import com.backend.trackinvest.usuarios.application.ports.in.dto.MessageDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PrivateController {

    @GetMapping("/private")
    public ResponseEntity<MessageDTO> privateMessage(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(new MessageDTO("This is a private endpoint. Hello: " + jwt.getClaim("name")));
    }
}

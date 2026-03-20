package com.backend.trackinvest.usuarios.infrastructure.adapter.in.controller;

import com.backend.trackinvest.usuarios.application.ports.in.dto.auth.TokenDTO;
import com.backend.trackinvest.usuarios.application.ports.in.dto.auth.UrlDTO;
import com.backend.trackinvest.usuarios.application.ports.in.service.AuthPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthPort authPort;

    @GetMapping("/url")
    public ResponseEntity<UrlDTO> url() {
        return ResponseEntity.ok(authPort.getAuthUrl());
    }

    @GetMapping("/callback")
    public ResponseEntity<TokenDTO> callback(@RequestParam("code") String code) {
        return ResponseEntity.ok(authPort.authenticateWithCode(code));
    }
}

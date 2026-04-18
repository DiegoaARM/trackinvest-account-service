package com.trackinvest.account.usuarios.infrastructure.adapter.in.controller;

import com.trackinvest.account.usuarios.application.ports.in.dto.auth.TokenDTO;
import com.trackinvest.account.usuarios.application.ports.in.dto.auth.UrlDTO;
import com.trackinvest.account.usuarios.application.ports.in.service.auth.AuthWithCodePort;
import com.trackinvest.account.usuarios.application.ports.in.service.auth.GenerateAuthUrlPort;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Endpoints para gestión del login y autenticación")
public class AuthController {

    private final GenerateAuthUrlPort generateAuthUrlPort;
    private final AuthWithCodePort authWithCodePort;

    @GetMapping("/url")
    public ResponseEntity<UrlDTO> url() {
        return ResponseEntity.ok(generateAuthUrlPort.execute());
    }

    @GetMapping("/callback")
    public ResponseEntity<TokenDTO> callback(@RequestParam("code") String code) {
        return ResponseEntity.ok(authWithCodePort.execute(code));
    }
}

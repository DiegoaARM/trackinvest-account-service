package com.backend.trackinvest.usuarios.infrastructure.adapter.in.controller;

import com.backend.trackinvest.usuarios.application.ports.in.dto.auth.TokenDTO;
import com.backend.trackinvest.usuarios.application.ports.in.dto.auth.UrlDTO;
import com.backend.trackinvest.usuarios.application.ports.in.service.auth.AuthWithCodePort;
import com.backend.trackinvest.usuarios.application.ports.in.service.auth.GenerateAuthUrlPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
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

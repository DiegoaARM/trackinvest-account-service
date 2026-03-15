package com.backend.trackinvest.usuarios.infrastructure.adapter.in.controller;

import com.backend.trackinvest.usuarios.application.ports.in.dto.AuthResponseDTO;
import com.backend.trackinvest.usuarios.application.ports.in.dto.GetUserDTO;
import com.backend.trackinvest.usuarios.application.ports.in.dto.LoginDTO;
import com.backend.trackinvest.usuarios.application.ports.in.dto.RegisterUserDTO;
import com.backend.trackinvest.usuarios.application.ports.in.service.AutenticateUserPort;
import com.backend.trackinvest.usuarios.application.ports.in.service.RegisterUserPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegisterUserPort registerUserUseCase;
    private final AutenticateUserPort loginUserUseCase;

    @PostMapping("/register")
    public ResponseEntity<GetUserDTO> register(@RequestBody RegisterUserDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(registerUserUseCase.execute(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO dto) {
        return ResponseEntity.ok(loginUserUseCase.execute(dto));
    }
}

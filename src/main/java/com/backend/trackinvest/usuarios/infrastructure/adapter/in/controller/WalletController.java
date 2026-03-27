package com.backend.trackinvest.usuarios.infrastructure.adapter.in.controller;

import com.backend.trackinvest.usuarios.application.ports.in.dto.wallet.CreateWalletRequestDTO;
import com.backend.trackinvest.usuarios.application.ports.in.dto.wallet.GetWalletResponseDTO;
import com.backend.trackinvest.usuarios.application.ports.in.service.wallet.CreateWalletPort;
import com.backend.trackinvest.usuarios.infrastructure.adapter.out.persistence.mapper.WalletEntityMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wallets")
@RequiredArgsConstructor
@Tag(name = "Wallet", description = "Endpoints para gestión de carteras")
public class WalletController {

    private final CreateWalletPort createWalletPort;
    private final WalletEntityMapper mapper;

    @PostMapping
    public ResponseEntity<GetWalletResponseDTO> createWallet(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CreateWalletRequestDTO request
    ) {
        // 1. Extraemos el cognito_id del token JWT
        // En AWS Cognito, el identificador único suele estar en el claim "sub"
        String cognitoId = jwt.getSubject();

        // 2. Ejecutamos el caso de uso
        // Este ya contiene la lógica de buscar al usuario y validar reglas
        GetWalletResponseDTO newWallet = createWalletPort.execute(cognitoId, request);

        // 4. Retornamos 201 Created
        return new ResponseEntity<>(newWallet, HttpStatus.CREATED);
    }
}

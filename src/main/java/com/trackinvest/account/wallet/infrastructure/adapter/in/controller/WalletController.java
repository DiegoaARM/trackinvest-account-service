package com.trackinvest.account.wallet.infrastructure.adapter.in.controller;

import com.trackinvest.account.wallet.application.ports.in.dto.CreateWalletRequestDTO;
import com.trackinvest.account.wallet.application.ports.in.dto.GetWalletResponseDTO;
import com.trackinvest.account.wallet.application.ports.in.dto.UpdateWalletRequestDTO;
import com.trackinvest.account.wallet.application.ports.in.dto.UpdateWalletBalanceRequestDTO;
import com.trackinvest.account.wallet.application.ports.in.service.CreateWalletPort;
import com.trackinvest.account.wallet.application.ports.in.service.UpdateWalletPort;
import com.trackinvest.account.wallet.application.ports.in.service.UpdateWalletBalancePort;
import com.trackinvest.account.wallet.application.ports.in.service.DeleteWalletPort;
import com.trackinvest.account.wallet.infrastructure.adapter.out.persistence.mapper.WalletEntityMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallets")
@RequiredArgsConstructor
@Tag(name = "Wallet", description = "Endpoints para gestión de carteras")
public class WalletController {

    private final CreateWalletPort createWalletPort;
    private final UpdateWalletPort updateWalletPort;
    private final UpdateWalletBalancePort updateWalletBalancePort;
    private final DeleteWalletPort deleteWalletPort;

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

        return new ResponseEntity<>(newWallet, HttpStatus.CREATED);
    }

    @PutMapping("/{walletId}")
    public ResponseEntity<GetWalletResponseDTO> updateWallet(
            @PathVariable java.util.UUID walletId,
            @Valid @RequestBody UpdateWalletRequestDTO request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        // 1. Extraemos el cognito_id del token JWT
        String cognitoId = jwt.getSubject();

        // 2. Ejecutamos el caso de uso
        GetWalletResponseDTO updatedWallet = updateWalletPort.execute(cognitoId, walletId, request);

        // 3. Retornamos 200 OK
        return ResponseEntity.ok(updatedWallet);
    }

    @PutMapping("/{walletId}/balance")
    public ResponseEntity<GetWalletResponseDTO> updateWalletBalance(
            @PathVariable java.util.UUID walletId,
            @Valid @RequestBody UpdateWalletBalanceRequestDTO request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        // 1. Extraemos el cognito_id del token JWT
        String cognitoId = jwt.getSubject();

        // 2. Ejecutamos el caso de uso
        GetWalletResponseDTO updatedWallet = updateWalletBalancePort.execute(cognitoId, walletId, request);

        // 3. Retornamos 200 OK
        return ResponseEntity.ok(updatedWallet);
    }

    @DeleteMapping("/{walletId}")
    public ResponseEntity<Void> deleteWallet(
            @PathVariable java.util.UUID walletId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        // 1. Extraemos el cognito_id del token JWT
        String cognitoId = jwt.getSubject();

        // 2. Ejecutamos el caso de uso
        deleteWalletPort.execute(cognitoId, walletId);

        // 3. Retornamos 204 No Content
        return ResponseEntity.noContent().build();
    }
}

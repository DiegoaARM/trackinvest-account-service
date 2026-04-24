package com.trackinvest.account.wallet.infrastructure.adapter.in.controller;

import com.trackinvest.account.wallet.application.ports.in.dto.CreateWalletRequestDTO;
import com.trackinvest.account.wallet.application.ports.in.dto.GetWalletResponseDTO;
import com.trackinvest.account.wallet.application.ports.in.dto.UpdateWalletRequestDTO;
import com.trackinvest.account.wallet.application.ports.in.dto.UpdateWalletBalanceRequestDTO;
import com.trackinvest.account.wallet.application.ports.in.service.CreateWalletPort;
import com.trackinvest.account.wallet.application.ports.in.service.UpdateWalletPort;
import com.trackinvest.account.wallet.application.ports.in.service.UpdateWalletBalancePort;
import com.trackinvest.account.wallet.application.ports.in.service.DeleteWalletPort;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/wallets")
@RequiredArgsConstructor
@Tag(name = "Wallet", description = "Endpoints for wallets management")
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
        String cognitoId = jwt.getSubject();
        GetWalletResponseDTO newWallet = createWalletPort.execute(cognitoId, request);
        return new ResponseEntity<>(newWallet, HttpStatus.CREATED);
    }

    @PutMapping("/{walletId}")
    public ResponseEntity<GetWalletResponseDTO> updateWallet(
            @PathVariable UUID walletId,
            @Valid @RequestBody UpdateWalletRequestDTO request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String cognitoId = jwt.getSubject();
        GetWalletResponseDTO updatedWallet = updateWalletPort.execute(cognitoId, walletId, request);
        return ResponseEntity.ok(updatedWallet);
    }

    @PutMapping("/{walletId}/balance")
    public ResponseEntity<GetWalletResponseDTO> updateWalletBalance(
            @PathVariable UUID walletId,
            @Valid @RequestBody UpdateWalletBalanceRequestDTO request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String cognitoId = jwt.getSubject();
        GetWalletResponseDTO updatedWallet = updateWalletBalancePort.execute(cognitoId, walletId, request);
        return ResponseEntity.ok(updatedWallet);
    }

    @DeleteMapping("/{walletId}")
    public ResponseEntity<Void> deleteWallet(
            @PathVariable UUID walletId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String cognitoId = jwt.getSubject();
        deleteWalletPort.execute(cognitoId, walletId);
        return ResponseEntity.noContent().build();
    }
}

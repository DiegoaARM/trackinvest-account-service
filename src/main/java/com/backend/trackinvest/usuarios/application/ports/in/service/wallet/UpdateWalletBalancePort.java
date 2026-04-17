package com.backend.trackinvest.usuarios.application.ports.in.service.wallet;

import com.backend.trackinvest.usuarios.application.ports.in.dto.wallet.GetWalletResponseDTO;
import com.backend.trackinvest.usuarios.application.ports.in.dto.wallet.UpdateWalletBalanceRequestDTO;

import java.util.UUID;

public interface UpdateWalletBalancePort {
    GetWalletResponseDTO execute(String cognitoId, UUID walletId, UpdateWalletBalanceRequestDTO request);
}

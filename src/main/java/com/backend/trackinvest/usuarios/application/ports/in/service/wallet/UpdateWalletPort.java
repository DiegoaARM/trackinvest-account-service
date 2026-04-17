package com.backend.trackinvest.usuarios.application.ports.in.service.wallet;

import com.backend.trackinvest.usuarios.application.ports.in.dto.wallet.GetWalletResponseDTO;
import com.backend.trackinvest.usuarios.application.ports.in.dto.wallet.UpdateWalletRequestDTO;

import java.util.UUID;

public interface UpdateWalletPort {
    GetWalletResponseDTO execute(String cognitoId, UUID walletId, UpdateWalletRequestDTO request);
}

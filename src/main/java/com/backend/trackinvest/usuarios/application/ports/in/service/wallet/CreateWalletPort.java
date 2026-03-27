package com.backend.trackinvest.usuarios.application.ports.in.service.wallet;

import com.backend.trackinvest.usuarios.application.ports.in.dto.wallet.CreateWalletRequestDTO;
import com.backend.trackinvest.usuarios.application.ports.in.dto.wallet.GetWalletResponseDTO;

public interface CreateWalletPort {
    GetWalletResponseDTO execute(String cognitoId, CreateWalletRequestDTO wallet);
}

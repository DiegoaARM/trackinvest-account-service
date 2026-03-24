package com.backend.trackinvest.usuarios.application.ports.in.service.wallet;

import com.backend.trackinvest.usuarios.application.ports.in.dto.wallet.GetWalletResponseDTO;

public interface CreateWalletPort {
    void execute(String userCognitoId, GetWalletResponseDTO wallet);
}

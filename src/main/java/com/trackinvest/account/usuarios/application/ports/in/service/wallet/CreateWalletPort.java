package com.trackinvest.account.usuarios.application.ports.in.service.wallet;

import com.trackinvest.account.usuarios.application.ports.in.dto.wallet.CreateWalletRequestDTO;
import com.trackinvest.account.usuarios.application.ports.in.dto.wallet.GetWalletResponseDTO;

public interface CreateWalletPort {
    GetWalletResponseDTO execute(String cognitoId, CreateWalletRequestDTO wallet);
}

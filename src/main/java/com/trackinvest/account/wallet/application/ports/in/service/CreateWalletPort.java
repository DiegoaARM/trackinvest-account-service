package com.trackinvest.account.wallet.application.ports.in.service;

import com.trackinvest.account.wallet.application.ports.in.dto.CreateWalletRequestDTO;
import com.trackinvest.account.wallet.application.ports.in.dto.GetWalletResponseDTO;

public interface CreateWalletPort {
    GetWalletResponseDTO execute(String cognitoId, CreateWalletRequestDTO wallet);
}

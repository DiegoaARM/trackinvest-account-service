package com.trackinvest.account.wallet.application.ports.in.service;

import com.trackinvest.account.wallet.application.ports.in.dto.GetWalletResponseDTO;
import com.trackinvest.account.wallet.application.ports.in.dto.UpdateWalletRequestDTO;

import java.util.UUID;

public interface UpdateWalletPort {
    GetWalletResponseDTO execute(String cognitoId, UUID walletId, UpdateWalletRequestDTO request);
}

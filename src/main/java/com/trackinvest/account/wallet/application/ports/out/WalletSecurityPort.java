package com.trackinvest.account.wallet.application.ports.out;

import java.util.UUID;

public interface WalletSecurityPort {
    void validateWalletOwnership(UUID walletId, String cognitoId);
}

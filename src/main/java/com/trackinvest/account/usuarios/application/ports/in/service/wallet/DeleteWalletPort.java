package com.trackinvest.account.usuarios.application.ports.in.service.wallet;

import java.util.UUID;

public interface DeleteWalletPort {
    void execute(String cognitoId, java.util.UUID walletId);
}
